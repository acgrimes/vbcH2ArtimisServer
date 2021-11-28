package com.ll.vbc.business.services.server.consensus.leader.handler;

import com.ll.vbc.messageService.request.GeneralRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LeaderHeartbeatFollowerResponseHandler {

    private static final Logger log = LoggerFactory.getLogger(LeaderHeartbeatFollowerResponseHandler.class);

    /**
     *
     * @param generalRequest
     */
    public void handleFollowerHeartbeatResponse(GeneralRequest generalRequest) {

        log.info("handleFollowerHeartbeatResponse -  Follower request response: "+generalRequest.getRequest().name());
    }

}
