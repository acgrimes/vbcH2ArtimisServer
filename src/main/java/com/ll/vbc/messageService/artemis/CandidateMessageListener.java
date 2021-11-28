package com.ll.vbc.messageService.artemis;

import com.ll.vbc.business.services.server.consensus.follower.handler.FollowerCandidateVoteRequestHandler;
import com.ll.vbc.domain.ConsensusServer;
import com.ll.vbc.enums.Request;
import com.ll.vbc.messageService.request.GeneralRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

public class CandidateMessageListener implements MessageListener {

    private static final Logger log = LoggerFactory.getLogger(CandidateMessageListener.class);

    private final FollowerCandidateVoteRequestHandler followerCandidateVoteRequestHandler;

    public CandidateMessageListener() {
        followerCandidateVoteRequestHandler = new FollowerCandidateVoteRequestHandler();
    }

    /**
     * This method handles messages coming into the Candidate Subscriber. Only VBC servers in the follower state
     * process these Candidate messages.
     * @param message
     */
    @Override
    public void onMessage(Message message) {
        try {
            GeneralRequest generalRequest = message.getBody(GeneralRequest.class);
            Request request = generalRequest.getRequest();
            log.info("CandidateMessageListener: "+generalRequest.toString());
            switch(ConsensusServer.getState()) {
                case Follower: {
                    switch(request) {
                        case RequestLeaderVote: {
                            log.info("onMessage - Follower State: "+generalRequest.toString());
                            followerCandidateVoteRequestHandler.handleCandidateVoteRequest(generalRequest);
                            break;
                        }
                        default: {
                            log.warn("onMessage Follower State - Incorrect messageType: "+request.name());
                            break;
                        }
                    }
                    break;
                }
                default: {
                    log.warn("onMessage InCorrect state: "+ConsensusServer.getState());
                    break;
                }
            }
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
