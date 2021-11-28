package com.ll.vbc.messageService.artemis;

import com.ll.vbc.messageService.request.GeneralRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.CompletionListener;
import javax.jms.JMSException;
import javax.jms.Message;

public class FollowerPublisherCompletionListener implements CompletionListener {

    private static final Logger log = LoggerFactory.getLogger(FollowerPublisherCompletionListener.class);

    @Override
    public void onCompletion(Message message) {
        try {
            log.info("FollowerPublisherCompletionListener: "+message.getBody(GeneralRequest.class));
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onException(Message message, Exception exception) {
        log.warn("FollowerPublisherCompletionListener: "+exception.getMessage());
    }
}
