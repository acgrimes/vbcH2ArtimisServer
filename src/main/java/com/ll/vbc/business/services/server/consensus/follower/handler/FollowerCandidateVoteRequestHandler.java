package com.ll.vbc.business.services.server.consensus.follower.handler;

import com.ll.vbc.enums.Request;
import com.ll.vbc.messageService.artemis.JmsManager;
import com.ll.vbc.messageService.request.GeneralRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FollowerCandidateVoteRequestHandler {

    private static final Logger log = LoggerFactory.getLogger(FollowerCandidateVoteRequestHandler.class);

    private static final JmsManager jmsManager = JmsManager.getInstance();

    public FollowerCandidateVoteRequestHandler() {  }

    /**
     *
     * @param generalRequest
     */
    public void handleCandidateVoteRequest(final GeneralRequest generalRequest) {

        log.info("handleCandidateVoteRequest: "+generalRequest);

        final GeneralRequest gr = new GeneralRequest(Request.FollowerGrantsLeaderVote,
                                                     generalRequest.getAppendEntry(), new byte[0], new byte[0]);
        log.info("handleCandidateVoteRequest: gr = "+gr.toString());
        JmsManager.getInstance().sendFollowerMessage(gr);

        //TODO: send LeaderNotification to the Proxy server
    }

}
