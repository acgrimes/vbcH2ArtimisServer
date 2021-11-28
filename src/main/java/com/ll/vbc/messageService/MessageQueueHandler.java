package com.ll.vbc.messageService;

import com.ll.vbc.business.services.server.election.leader.ElectionTransactionHandler;
import com.ll.vbc.messageService.request.GeneralRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import java.util.Objects;
import java.util.concurrent.ArrayBlockingQueue;


public class MessageQueueHandler implements MessageListener, Runnable {

    private static final Logger log = LoggerFactory.getLogger(MessageQueueHandler.class);
    private static int referenceCount;
    private static final ArrayBlockingQueue<GeneralRequest> generalRequestQueue = new ArrayBlockingQueue<GeneralRequest>(1000000);

    private final ElectionTransactionHandler electionTransactionHandler;

    public MessageQueueHandler() {
        electionTransactionHandler = new ElectionTransactionHandler();
    }

    @Override
    public void run() {
        log.debug("Entering run()");

        while(true) {
            GeneralRequest generalRequest = null;
            try {
                generalRequest = generalRequestQueue.take();

                switch (Objects.requireNonNull(generalRequest).getRequest()) {
                    case ElectionTransaction: {
                        electionTransactionHandler.electionTransactionResponse(generalRequest);
                        log.debug("GeneralRequest: "+(++referenceCount));
                        break;
                    }
                    default: {
                        log.error("InValid Request Type");
                    }
                }
            } catch (Exception ie) {
                ie.printStackTrace();
            }
        }
    }

    @Override
    public void onMessage(Message message) {

        GeneralRequest generalRequest;
        try {
            generalRequest = message.getBody(GeneralRequest.class);
            generalRequestQueue.put(generalRequest);
        } catch (JMSException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
