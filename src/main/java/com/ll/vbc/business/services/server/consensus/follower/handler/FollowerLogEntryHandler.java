package com.ll.vbc.business.services.server.consensus.follower.handler;

import com.ll.vbc.dao.consensus.ConsensusLogDao;
import com.ll.vbc.domain.AppendEntry;
import com.ll.vbc.domain.ConsensusLog;
import com.ll.vbc.enums.Request;
import com.ll.vbc.messageService.artemis.JmsManager;
import com.ll.vbc.messageService.request.GeneralRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.concurrent.ArrayBlockingQueue;

public class FollowerLogEntryHandler implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(FollowerLogEntryHandler.class);

    private final ConsensusLogDao consensusLogDao;
    private final ArrayBlockingQueue<AppendEntry> appendEntryArrayBlockingQueue;
    private final JmsManager jmsManager;

    public FollowerLogEntryHandler() {
        consensusLogDao = new ConsensusLogDao();
        appendEntryArrayBlockingQueue = new ArrayBlockingQueue<>(1000);
        jmsManager = JmsManager.getInstance();
    }

    public void handleLeaderLogEntry(GeneralRequest generalRequest) {

        log.info("handleLeaderLogEntry: "+generalRequest.toString());
        try {
            appendEntryArrayBlockingQueue.put(generalRequest.getAppendEntry());
        } catch(Exception ex) {
            log.warn("handleLeaderLogEntry - "+ex.getMessage());
            ex.printStackTrace();
        }
    }

    @Override
    public void run() {

        while(true) {
            try {
                AppendEntry appendEntry = appendEntryArrayBlockingQueue.take();
                log.info("run - "+appendEntry.toString());
                int result = consensusLogDao.save(appendEntry);
                Request request = Request.FollowerLogEntry;
                if (result==0) {
                    request = Request.FollowerLogEntryFailure;
                }
                GeneralRequest generalRequest = new GeneralRequest(request,
                                                                   appendEntry,
                                                          new byte[0], new byte[0]);

                JmsManager.getInstance().sendFollowerMessage(generalRequest);
            } catch(Exception ex) {
                log.warn("call - "+ex.getMessage());
                ex.printStackTrace();
            }
        }

    }
}
