package com.ll.vbc.business.services.server.consensus.candidate.handler;

import com.ll.vbc.business.services.client.consensus.leader.request.LeaderNotificationToFollowerRequest;
import com.ll.vbc.business.services.client.consensus.leader.request.LeaderNotificationToProxyRequest;
import com.ll.vbc.business.services.client.consensus.scheduling.Scheduler;
import com.ll.vbc.business.services.server.consensus.follower.handler.FollowerCandidateVoteRequestHandler;
import com.ll.vbc.domain.ConsensusServer;
import com.ll.vbc.domain.ConsensusState;
import com.ll.vbc.enums.Request;
import com.ll.vbc.enums.ServerConsensusState;
import com.ll.vbc.messageService.artemis.JmsManager;
import com.ll.vbc.messageService.request.GeneralRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CandidateRequestHandler {

    private static final Logger log = LoggerFactory.getLogger(FollowerCandidateVoteRequestHandler.class);

    private final Scheduler scheduler;
    private final LeaderNotificationToFollowerRequest leaderNotificationToFollowerRequest;
    private final LeaderNotificationToProxyRequest leaderNotificationToProxyRequest;
    private final double followerGrantLevel = Math.ceil(ConsensusState.getServerList().size()/2.0);


    public CandidateRequestHandler() {
        this.scheduler = Scheduler.getInstance();
        leaderNotificationToFollowerRequest = new LeaderNotificationToFollowerRequest();
        leaderNotificationToProxyRequest = new LeaderNotificationToProxyRequest();
    }

    public void handleFollowerCandidateVoteResponse(GeneralRequest generalRequest) {

        log.info("handleFollowerCandidateVoteResponse - followerGrantLevel "+followerGrantLevel);
        if(generalRequest.getRequest()== Request.FollowerGrantsLeaderVote) {
            ConsensusServer.setLeaderVotes(ConsensusServer.getLeaderVotes()+1);
            log.info("FollowerGrantsLeaderVote: "+ConsensusServer.getLeaderVotes());
        }
        if((double)(ConsensusServer.getLeaderVotes())>=followerGrantLevel) {
            log.info("Candidate Votes "+ConsensusServer.getLeaderVotes());
            ConsensusServer.setState(ServerConsensusState.Leader);
            scheduler.cancelFollowerHeartBeatTimeoutTimer();
            scheduler.startLeaderHeartBeatTimer();
            JmsManager.getInstance().closeLeaderSubscriber();
            JmsManager.getInstance().closeFollowerSubscriber();

            //reopen FollowerSubscriber with a differnet MessageSelector:
            JmsManager.getInstance().configureFollowerSubscriber();
            JmsManager.getInstance().configureProxySubscriber();
            //TODO: send LeaderNotification to the Proxy server
//            leaderNotificationToProxyRequest.sendProxyLeaderNotification(generalRequest);
            leaderNotificationToFollowerRequest.sendFollowerLeaderNotification(generalRequest);

        }
    }
}