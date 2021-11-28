package com.ll.vbc.business.services.client.consensus.scheduling;


import com.ll.vbc.business.services.client.consensus.follower.FollowerTransitionToCandidate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.TimerTask;

public class FollowerHeartBeatTimeoutTask extends TimerTask {

    private static final Logger log = LoggerFactory.getLogger(FollowerHeartBeatTimeoutTask.class);

    private FollowerTransitionToCandidate followerTransitionToCandidate;

    public FollowerHeartBeatTimeoutTask() {
        followerTransitionToCandidate = new FollowerTransitionToCandidate();
    }

    public void run() {
        log.info("Follower timeout of Leader Heartbeat");
        followerTransitionToCandidate.transition();
    }

}
