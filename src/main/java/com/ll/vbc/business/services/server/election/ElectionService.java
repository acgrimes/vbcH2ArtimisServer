package com.ll.vbc.business.services.server.election;


import com.ll.vbc.business.services.client.consensus.leader.event.LogEntryEvent;
import com.ll.vbc.business.services.client.consensus.leader.request.LeaderLogEntryRequest;
import com.ll.vbc.dao.consensus.ConsensusLogDao;
import com.ll.vbc.domain.AppendEntry;
import com.ll.vbc.domain.ConsensusState;
import com.ll.vbc.enums.Request;
import com.ll.vbc.messageService.request.GeneralRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicLong;

public final class ElectionService {

    private static final Logger log = LoggerFactory.getLogger(ElectionService.class);
    private final ConsensusLogDao consensusLogDao;
    private final LeaderLogEntryRequest leaderLogEntryRequest;

    public ElectionService() {
        consensusLogDao = new ConsensusLogDao();
        leaderLogEntryRequest = new LeaderLogEntryRequest();
    }

    public final void handleElectionTransaction(final GeneralRequest generalRequest) {

        log.debug("handleElectionTransaction: "+generalRequest);

        ConsensusState.setCurrentIndex(new AtomicLong(ConsensusState.getCurrentIndex().incrementAndGet()));
        AppendEntry appendEntry = new AppendEntry(generalRequest.getAppendEntry().getServer(),
                                                    generalRequest.getAppendEntry().getvToken(),
                                                    ConsensusState.getCurrentIndex().get(),
                                                    ConsensusState.getCurrentTerm().get(),
                                                    generalRequest.getAppendEntry().getElectionTransaction(),
                                                    new byte[0]);
        try {
            if(validateElectionRequest(generalRequest)) {
                consensusLogDao.save(appendEntry);
                GeneralRequest genRequest = new GeneralRequest(Request.LeaderLogEntry,
                                                    appendEntry,
                                                    generalRequest.getPublicKey(),
                                                    generalRequest.getDigitalSignature());
                leaderLogEntryRequest.onApplicationEvent(new LogEntryEvent(genRequest));
            } else {
                log.warn("handleElectionTransaction - INVALID GeneralRequest: "+generalRequest.toString());
            }
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     *
     * @param electionRequest
     * @return
     */
    protected final boolean validateElectionRequest(final GeneralRequest electionRequest) {

        boolean result = false;
        byte[] electionTx = electionRequest.getAppendEntry().getElectionTransaction();
        byte[] encodedPubKey = electionRequest.getPublicKey();
        byte[] txSignature = electionRequest.getDigitalSignature();

        ValidateDigitalSignature signature = new ValidateDigitalSignature();
        if(signature.isValid(electionTx, encodedPubKey, txSignature)&&(electionRequest.getAppendEntry().getvToken()!=null)) {
            result = true;
        } else {
            result = false;
        }
        return result;
    }
}
