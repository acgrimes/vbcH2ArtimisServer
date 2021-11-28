package com.ll.vbc.business.services.client.consensus.follower;

import com.ll.vbc.business.services.client.consensus.candidate.CandidateVoteRequest;
import com.ll.vbc.business.services.client.consensus.scheduling.Scheduler;
import com.ll.vbc.domain.ConsensusServer;
import com.ll.vbc.enums.ServerConsensusState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FollowerTransitionToCandidate {

    private static final Logger log = LoggerFactory.getLogger(FollowerTransitionToCandidate.class.getSimpleName());
    private final CandidateVoteRequest candidateVoteRequest;
    private final Scheduler scheduler;

    public FollowerTransitionToCandidate() {
        candidateVoteRequest = new CandidateVoteRequest();
        scheduler = Scheduler.getInstance();

    }

    public void transition() {
        log.info("Follower Transitioning to Candidate");
        ConsensusServer.setState(ServerConsensusState.Candidate);
        scheduler.cancelFollowerHeartBeatTimeoutTimer();
        candidateVoteRequest.sendCandidateLeaderVoteRequest();

    }
}