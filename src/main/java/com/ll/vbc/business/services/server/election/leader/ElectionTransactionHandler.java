package com.ll.vbc.business.services.server.election.leader;

import com.ll.vbc.business.services.server.election.ElectionService;
import com.ll.vbc.messageService.request.GeneralRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ElectionTransactionHandler {

    private static final Logger log = LoggerFactory.getLogger(ElectionTransactionHandler.class);

    private ElectionService electionService;

    public ElectionTransactionHandler() {
        electionService = new ElectionService();
    }

    public void electionTransactionResponse(GeneralRequest generalRequest) {
        log.debug("Entering electionTransactionResponse: "+generalRequest.toString());
        try {
            electionService.handleElectionTransaction(generalRequest);
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }

}
