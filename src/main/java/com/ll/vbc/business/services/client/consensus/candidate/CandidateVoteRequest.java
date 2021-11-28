package com.ll.vbc.business.services.client.consensus.candidate;


import com.ll.vbc.domain.AppendEntry;
import com.ll.vbc.domain.ConsensusServer;
import com.ll.vbc.domain.ConsensusState;
import com.ll.vbc.enums.Request;
import com.ll.vbc.messageService.artemis.JmsManager;
import com.ll.vbc.messageService.request.GeneralRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CandidateVoteRequest {

    private static final Logger log = LoggerFactory.getLogger(CandidateVoteRequest.class);

    private final JmsManager jmsManager;

    public CandidateVoteRequest() {
        jmsManager = JmsManager.getInstance();
    }

    /**
     *
     */
    public void sendCandidateLeaderVoteRequest() {

        log.info("Entering sendCandidateLeaderVoteRequest");
        AppendEntry appendEntry = new AppendEntry(ConsensusServer.getServerInstance(),
                                            null,
                                                    ConsensusState.getCurrentIndex().get(),
                                                    ConsensusState.getCurrentTerm().get(),
                                    null, null);
        GeneralRequest generalRequest = new GeneralRequest(Request.RequestLeaderVote,
                                                            appendEntry,null, null);
        jmsManager.configureFollowerSubscriber();
        jmsManager.sendCandidateMessage(generalRequest);
    }

}
