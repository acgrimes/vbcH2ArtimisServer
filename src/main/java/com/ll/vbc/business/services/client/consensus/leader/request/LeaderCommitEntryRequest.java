package com.ll.vbc.business.services.client.consensus.leader.request;

import com.ll.vbc.business.services.client.consensus.leader.event.CommitEntryEvent;
import com.ll.vbc.domain.AppendEntry;
import com.ll.vbc.enums.Request;
import com.ll.vbc.messageService.artemis.JmsManager;
import com.ll.vbc.messageService.request.GeneralRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LeaderCommitEntryRequest {

    private static final Logger log = LoggerFactory.getLogger(LeaderCommitEntryRequest.class);

    public LeaderCommitEntryRequest() { }

    public void onApplicationEvent(CommitEntryEvent commitEntryRequest) {

        log.debug("onApplicationEvent: "+ commitEntryRequest.getSource().toString());

        AppendEntry appendEntry = (AppendEntry) commitEntryRequest.getSource();
        GeneralRequest generalRequest = new GeneralRequest(Request.LeaderLogCommit, appendEntry, new byte[0], new byte[0]);
        JmsManager.getInstance().sendLeaderMessage(generalRequest);

    }

}
