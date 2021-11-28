package com.ll.vbc.business.services.client.consensus.leader.request;


import com.ll.vbc.business.services.client.consensus.leader.event.LogEntryEvent;
import com.ll.vbc.messageService.artemis.JmsManager;
import com.ll.vbc.messageService.request.GeneralRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class LeaderLogEntryRequest {

    private static final Logger log = LoggerFactory.getLogger(LeaderLogEntryRequest.class);

    public LeaderLogEntryRequest() { }

    public void onApplicationEvent(LogEntryEvent logEntryRequest) {

        log.debug("onApplicationEvent: "+ logEntryRequest.getSource().toString());

        GeneralRequest generalRequest = (GeneralRequest) logEntryRequest.getSource();
        JmsManager.getInstance().sendLeaderMessage(generalRequest);
    }
}
