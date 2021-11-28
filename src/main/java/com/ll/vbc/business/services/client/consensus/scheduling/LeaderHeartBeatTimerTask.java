package com.ll.vbc.business.services.client.consensus.scheduling;

import com.ll.vbc.business.services.client.consensus.leader.request.LeaderHeartbeatRequest;

import java.util.TimerTask;

public class LeaderHeartBeatTimerTask extends TimerTask {

    private LeaderHeartbeatRequest leaderHeartbeatRequest;

    public LeaderHeartBeatTimerTask() {
        leaderHeartbeatRequest = new LeaderHeartbeatRequest();
    }

    public void run() {
        leaderHeartbeatRequest.sendLeaderHeartbeatRequest();
    }

}
