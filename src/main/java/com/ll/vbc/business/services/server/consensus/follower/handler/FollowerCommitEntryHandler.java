package com.ll.vbc.business.services.server.consensus.follower.handler;

import com.ll.vbc.business.services.server.blockchain.BlockChainService;
import com.ll.vbc.domain.AppendEntry;
import com.ll.vbc.enums.Request;
import com.ll.vbc.messageService.artemis.JmsManager;
import com.ll.vbc.messageService.request.GeneralRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.concurrent.ArrayBlockingQueue;

public class FollowerCommitEntryHandler implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(FollowerCommitEntryHandler.class);

    private final BlockChainService blockChainService;
    private final ArrayBlockingQueue<AppendEntry> appendEntryArrayBlockingQueue;

    public FollowerCommitEntryHandler() {
        blockChainService = new BlockChainService();
        appendEntryArrayBlockingQueue = new ArrayBlockingQueue<>(1000);
    }

    public void handleLeaderCommitEntry(GeneralRequest generalRequest) {

        log.debug("handleLeaderCommitEntry: "+generalRequest.toString());
        try {
            appendEntryArrayBlockingQueue.put(generalRequest.getAppendEntry());
        } catch(Exception ex) {
            log.warn("handleLeaderCommitEntry - "+ex.getMessage());
            ex.printStackTrace();
        }

    }

    @Override
    public void run() {

        while(true) {
            try {
                AppendEntry appendEntry = appendEntryArrayBlockingQueue.take();
                log.debug("run - "+appendEntry.toString());
                Optional<AppendEntry> appendEntryOp = blockChainService.followerCommitEntryResponse(appendEntry.getIndex());
                if(appendEntryOp.isPresent()) {
                    GeneralRequest generalRequest = new GeneralRequest(Request.FollowerLogCommit,
                                                                        appendEntryOp.get(),
                                                                new byte[0], new byte[0]);
                    JmsManager.getInstance().sendFollowerMessage(generalRequest);
                }
            } catch(Exception ex) {
                log.warn("call - "+ex.getMessage());
                ex.printStackTrace();
            }
        }
    }
}
