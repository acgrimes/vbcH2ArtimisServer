package com.ll.vbc.business.services.server.consensus.leader.handler;

import com.ll.vbc.messageService.request.GeneralRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LeaderCommitEntryHandler {

    private static final Logger log = LoggerFactory.getLogger(LeaderCommitEntryHandler.class);

    /**
     * This method will loop back to the proxy server as each follower reports that it has committed the logEntry
     * to the blockchain state machine. When the proxy server receives this message, it will remove an item from
     * its committed ballot map.
     * @param generalRequest
     */
    public void handleFollowerCommitEntry(GeneralRequest generalRequest) {

        log.debug("handleFollowerCommitEntry: "+generalRequest.toString());

    }

}
