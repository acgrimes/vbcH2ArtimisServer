package com.ll.vbc.messageService.artemis;

import com.ll.vbc.messageService.request.GeneralRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.CompletionListener;
import javax.jms.JMSException;
import javax.jms.Message;

public class ProxyPublisherCompletionListener implements CompletionListener {

    private static final Logger log = LoggerFactory.getLogger(ProxyPublisherCompletionListener.class);

    @Override
    public void onCompletion(Message message) {
        try {
        log.info("onCompletion: "+message.getBody(GeneralRequest.class).toString());
        } catch(JMSException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onException(Message message, Exception exception) {
        log.warn("onException: "+exception.getMessage());
    }
}
