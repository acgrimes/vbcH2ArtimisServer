package com.ll.vbc.business.services.client.consensus.scheduling;

import com.ll.vbc.domain.ConsensusServer;
import com.ll.vbc.messageService.artemis.JmsManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Timer;


public class Scheduler {

    private static Logger log = LoggerFactory.getLogger(Scheduler.class);
    private static final Scheduler INSTANCE = new Scheduler();
    public static Scheduler getInstance() {
        return INSTANCE;
    }

    private Timer leaderHeartBeatTimer;
    private Timer followerHeartBeatTimer;
    private Timer leaderElectionTimeoutTimer;

    private Scheduler() {    }

    /**
     * This method starts a timer that on timeout a task is run that sends a HeartBeat message from the Leader
     * server to all the follower servers.
     */
    public synchronized void startLeaderHeartBeatTimer() {
        log.info("Entering startLeaderHeartBeatTimer");
        leaderHeartBeatTimer = new Timer();
        leaderHeartBeatTimer.scheduleAtFixedRate(new LeaderHeartBeatTimerTask(), 2000L, 2000L);
    }

    public synchronized void cancelLeaderHeartBeatTimer() {
        if(leaderHeartBeatTimer!=null) {
            leaderHeartBeatTimer.cancel();
            leaderHeartBeatTimer = null;
        }
    }

    /**
     * This method starts a follower timer used to determine if a leader election should occur,
     * that is, if this timer times out, if this server is in a follower state, it should promote
     * itself to candidate state and request votes.
     */
    public synchronized void startFollowerHeartBeatTimeoutTimer() {
        log.info("Entering startFollowerHeartBeatTimeoutTimer");
        followerHeartBeatTimer = new Timer();
        if(ConsensusServer.getId().equals("A")) {
            followerHeartBeatTimer.scheduleAtFixedRate(new FollowerHeartBeatTimeoutTask(), 15000L, 3000L);
        } else {
            followerHeartBeatTimer.scheduleAtFixedRate(new FollowerHeartBeatTimeoutTask(), 30000L, 3000L);
        }
    }

    public synchronized void cancelFollowerHeartBeatTimeoutTimer() {
        log.info("Entering cancelFollowerHeartBeatTimeoutTimer");
        if(followerHeartBeatTimer!=null) {
            followerHeartBeatTimer.cancel();
            followerHeartBeatTimer = null;
        }
    }


    public void startLeaderElectionTimeoutTimer() {

        leaderElectionTimeoutTimer = new Timer();
        leaderElectionTimeoutTimer.scheduleAtFixedRate(new FollowerHeartBeatTimeoutTask(), 2000L, 3000L);
    }

    public void cancelLeaderElectionTimeoutTimer() {
        if(leaderElectionTimeoutTimer!=null) {
            leaderElectionTimeoutTimer.cancel();
            leaderElectionTimeoutTimer = null;
        }
    }
}
