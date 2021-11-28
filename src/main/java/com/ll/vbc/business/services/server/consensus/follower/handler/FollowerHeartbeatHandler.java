package com.ll.vbc.business.services.server.consensus.follower.handler;

import com.ll.vbc.business.services.client.consensus.scheduling.Scheduler;
import com.ll.vbc.dao.consensus.ConsensusLogDao;
import com.ll.vbc.enums.Request;
import com.ll.vbc.messageService.artemis.JmsManager;
import com.ll.vbc.messageService.request.GeneralRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FollowerHeartbeatHandler {

    private static final Logger log = LoggerFactory.getLogger(FollowerHeartbeatHandler.class);

    private final Scheduler scheduler;
    private final ConsensusLogDao consensusLogDao;

    public FollowerHeartbeatHandler() {
        scheduler = Scheduler.getInstance();
        consensusLogDao = new ConsensusLogDao();
    }

    public void handleLeaderHeartbeatRequest(GeneralRequest generalRequest) {

        log.info("Entering handleLeaderHeartbeatRequest()");

        scheduler.cancelFollowerHeartBeatTimeoutTimer();
        scheduler.startFollowerHeartBeatTimeoutTimer();

//        log.info("Before Mongo call");
//        // if AppendEntry>>ConsensusLog is not in db then follower will create the log entry and return
//        // but if AppendEntry>>ConsensusLog is in db, then client gets a valid ConsensusLog returned.
//        Optional<ConsensusLog> consensusLogOp = consensusLogDao.find(generalRequest.getAppendEntry().getIndex());
//        if(consensusLogOp.isEmpty()) {
//            consensusLogDao.save(generalRequest.getAppendEntry());
//        }
//        log.info("After Mongo call");
        sendHeartbeatFollowerResponse(generalRequest);
    }

    protected void sendHeartbeatFollowerResponse(GeneralRequest generalRequest) {

        try {
            GeneralRequest followerGeneralRequest = new GeneralRequest(Request.HeartbeatFollowerResponse,
                    generalRequest.getAppendEntry(),
                    new byte[0], new byte[0]);

            log.info("sendHeartbeatFollowerResponse: " + followerGeneralRequest.toString());
            JmsManager.getInstance().sendFollowerMessage(followerGeneralRequest);
        } catch(Exception ex) {
            log.warn(ex.getLocalizedMessage());
            if(JmsManager.getInstance()==null) log.warn("jmsManager is null");
        }
    }
}
