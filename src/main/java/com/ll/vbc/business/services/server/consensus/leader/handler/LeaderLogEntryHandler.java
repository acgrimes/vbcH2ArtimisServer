package com.ll.vbc.business.services.server.consensus.leader.handler;

import com.ll.vbc.business.services.client.consensus.leader.event.CommitEntryEvent;
import com.ll.vbc.business.services.client.consensus.leader.request.LeaderCommitEntryRequest;
import com.ll.vbc.business.services.server.blockchain.BlockChainService;
import com.ll.vbc.domain.AppendEntry;
import com.ll.vbc.domain.ConsensusState;
import com.ll.vbc.messageService.request.GeneralRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

public class LeaderLogEntryHandler implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(LeaderLogEntryHandler.class);
    private static int QCount, FCount;
    private final double minimumFollowers = Math.floor(ConsensusState.getServerList().size()/2.0);
    private final BlockChainService blockChainService;
    private final LeaderCommitEntryRequest leaderCommitEntryRequest;
    private final ArrayBlockingQueue<AppendEntry> indexQueue;
    private final Object mutex;

    public LeaderLogEntryHandler() {
        blockChainService = new BlockChainService();
        leaderCommitEntryRequest = new LeaderCommitEntryRequest();
        indexQueue = new ArrayBlockingQueue<>(1000000);
        mutex = new Object();
    }


    public void handleFollowerLogEntry(GeneralRequest generalRequest) {

        log.info("handleFollowerLogEntry: "+generalRequest.toString());

        synchronized (mutex) {
            indexQueue.add(generalRequest.getAppendEntry());
        }
    }

    public void run() {

        while(true) {
            try {
                AppendEntry entry = indexQueue.take();
                log.info("run after read queue count: "+(++QCount));
                if (ConsensusState.getLogEntryMap().get(entry.getIndex()) == null) {
                    List<AppendEntry> logEntryList = new ArrayList<>();
                    logEntryList.add(entry);
                    ConsensusState.getLogEntryMap().put(entry.getIndex(), logEntryList);
                } else {
                    ConsensusState.getLogEntryMap().get(entry.getIndex()).add(entry);
                }
                // If majority of followers have logged the election transaction, then notify followers to commit Tx.
                if ((double)(ConsensusState.getLogEntryMap().get(entry.getIndex()).size())>minimumFollowers) {
                    log.info("run - index >minimumFollowers Follower count: "+(++FCount));
                    blockChainService.followerCommitEntryResponse(entry.getIndex()).ifPresentOrElse(ae -> {
//                        ConsensusState.setCommitIndex(new AtomicLong(ConsensusState.getCurrentIndex().get()));
                        leaderCommitEntryRequest.onApplicationEvent(new CommitEntryEvent(ae));
                    }, () -> log.warn("run() - returned AppendEntry is empty"));
                }
            } catch(Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
