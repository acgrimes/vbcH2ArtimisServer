package com.ll.vbc.business.services.server.consensus.follower.handler;

import com.ll.vbc.messageService.request.GeneralRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FollowerLeaderNotificationHandler {

    private static final Logger log = LoggerFactory.getLogger(FollowerLeaderNotificationHandler.class);

    public void handleLeaderNotification(GeneralRequest generalRequest) {

        log.debug("handleLeaderNotification: "+generalRequest.toString());

    }

}
