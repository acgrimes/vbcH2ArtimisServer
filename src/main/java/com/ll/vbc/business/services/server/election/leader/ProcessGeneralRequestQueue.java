package com.ll.vbc.business.services.server.election.leader;

import com.ll.vbc.messageService.request.GeneralRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.concurrent.Callable;

public class ProcessGeneralRequestQueue implements Callable {

    private static final Logger log = LoggerFactory.getLogger(ProcessGeneralRequestQueue.class);
    private static int referenceCount;

    private final ElectionTransactionHandler electionTransactionHandler;

    public ProcessGeneralRequestQueue() {
        electionTransactionHandler = new ElectionTransactionHandler();
    }

    @Override
    public Object call() throws Exception {
        log.info("Entering call()");

        GeneralRequest generalRequest = null;
        while(true) {

            try {
                generalRequest = ProxyMessageQueue.generalRequestQueue.take();

                switch (Objects.requireNonNull(generalRequest).getRequest()) {
                    case ElectionTransaction: {
                        electionTransactionHandler.electionTransactionResponse(generalRequest);
                        log.info("GeneralRequest: "+(++referenceCount));
                        break;
                    }
                    default: {
                        log.error("InValid Request Type");
                    }
                }
            } catch (Exception ie) {
                ie.printStackTrace();
                throw new Exception();
            }
        }
    }
}
