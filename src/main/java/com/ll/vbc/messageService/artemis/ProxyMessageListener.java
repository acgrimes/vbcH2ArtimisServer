package com.ll.vbc.messageService.artemis;

import com.ll.vbc.business.services.server.election.leader.ProxyMessageQueue;
import com.ll.vbc.messageService.request.GeneralRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

public class ProxyMessageListener implements MessageListener {

    private static final Logger log = LoggerFactory.getLogger(ProxyMessageListener.class);

    @Override
    public void onMessage(Message message) {

        GeneralRequest generalRequest;
        try {
            generalRequest = message.getBody(GeneralRequest.class);
            log.info("Proxy - onMessage: "+generalRequest.getRequest().name());
            ProxyMessageQueue.generalRequestQueue.put(generalRequest);
        } catch (JMSException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
