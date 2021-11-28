package com.ll.vbc.messageService.artemis;

import com.ll.vbc.business.services.server.consensus.candidate.handler.CandidateRequestHandler;
import com.ll.vbc.business.services.server.consensus.leader.handler.*;
import com.ll.vbc.domain.ConsensusServer;
import com.ll.vbc.enums.Request;
import com.ll.vbc.messageService.request.GeneralRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class FollowerMessageListener implements MessageListener {

    private static final Logger log = LoggerFactory.getLogger(FollowerMessageListener.class);

    private final LeaderHeartbeatFollowerResponseHandler leaderHeartbeatFollowerResponseHandler;
    private final LeaderCommitEntryFailureHandler leaderCommitEntryFailureHandler;
    private final LeaderCommitEntryHandler leaderCommitEntryHandler;
    private final LeaderLogEntryHandler leaderLogEntryHandler;
    private final LeaderLogEntryFailureHandler leaderLogEntryFailureHandler;
    private final CandidateRequestHandler candidateRequestHandler;

    public FollowerMessageListener() {
        leaderHeartbeatFollowerResponseHandler = new LeaderHeartbeatFollowerResponseHandler();
        leaderCommitEntryFailureHandler = new LeaderCommitEntryFailureHandler();
        leaderCommitEntryHandler = new LeaderCommitEntryHandler();
        leaderLogEntryHandler = new LeaderLogEntryHandler();
        leaderLogEntryFailureHandler = new LeaderLogEntryFailureHandler();
        candidateRequestHandler = new CandidateRequestHandler();
        executeRunnableHandlers();
    }

    private void executeRunnableHandlers() {

        ExecutorService leaderLogEntryHandlerTask = Executors.newSingleThreadExecutor();
        Future<?> leaderLogEntryHandlerFuture = leaderLogEntryHandlerTask.submit(leaderLogEntryHandler);

    }

    /**
     * This method handles messages coming into the Follower Subscriber
     * @param message
     */
    @Override
    public void onMessage(Message message) {

        try {
            GeneralRequest generalRequest = message.getBody(GeneralRequest.class);
            Request request = generalRequest.getRequest();
            log.info("onMessage - State: "+ConsensusServer.getState()+", Request: "+request.name());
            switch(ConsensusServer.getState()) {
                case Leader: {
                    switch(request) {
                        case HeartbeatFollowerResponse: {
                            leaderHeartbeatFollowerResponseHandler.handleFollowerHeartbeatResponse(generalRequest);
                            break;
                        }
                        case FollowerLogEntry: {
                            leaderLogEntryHandler.handleFollowerLogEntry(generalRequest);
                            break;
                        }
                        case FollowerLogCommit: {
                            leaderCommitEntryHandler.handleFollowerCommitEntry(generalRequest);
                            break;
                        }
                        default: {
                            log.warn("onMessage Leader - InCorrect messageType: "+request.name());
                            break;
                        }
                    }
                    break;
                }
                case Candidate: {
                    switch(request) {
                        case FollowerGrantsLeaderVote: {
                            log.info("onMessage - Candidate State, FollowerGrantsLeaderVote messageType");
                            candidateRequestHandler.handleFollowerCandidateVoteResponse(generalRequest);
                            break;
                        }
                        case FollowerDeniesLeaderVote: {

                            break;
                        }
                        default: {
                            log.warn("onMessage Candidate State - InCorrect messageType: "+request.name());
                        }
                    }
                    break;
                }
                default: {
                    log.warn("onMessage - State is NOT Leader or Candidate: "+ConsensusServer.getState().name());
                }
            }
//            log.info("FollowerMessageListener: "+generalRequest.toString());
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
