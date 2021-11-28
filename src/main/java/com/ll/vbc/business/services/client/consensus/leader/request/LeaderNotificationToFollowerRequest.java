package com.ll.vbc.business.services.client.consensus.leader.request;

import com.ll.vbc.domain.AppendEntry;
import com.ll.vbc.domain.ConsensusServer;
import com.ll.vbc.enums.Request;
import com.ll.vbc.messageService.artemis.JmsManager;
import com.ll.vbc.messageService.request.GeneralRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LeaderNotificationToFollowerRequest {

    private static final Logger log = LoggerFactory.getLogger(LeaderNotificationToFollowerRequest.class);

    public LeaderNotificationToFollowerRequest() {}

    public void sendFollowerLeaderNotification(GeneralRequest generalRequest) {



        AppendEntry appendEntry = new AppendEntry(ConsensusServer.getServerInstance(),
                                                generalRequest.getAppendEntry().getvToken(),
                                                generalRequest.getAppendEntry().getIndex(),
                                                generalRequest.getAppendEntry().getTerm(),
                                                generalRequest.getAppendEntry().getElectionTransaction(),
                                                null);
        GeneralRequest genRequest = new GeneralRequest(Request.LeaderNotification, appendEntry, null, null);
        log.info(" sendFollowerLeaderNotification: "+genRequest.toString());
        JmsManager.getInstance().sendLeaderMessage(genRequest);

    }
}
