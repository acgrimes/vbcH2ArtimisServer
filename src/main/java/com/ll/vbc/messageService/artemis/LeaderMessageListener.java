package com.ll.vbc.messageService.artemis;

import com.ll.vbc.business.services.server.consensus.follower.handler.FollowerCommitEntryHandler;
import com.ll.vbc.business.services.server.consensus.follower.handler.FollowerHeartbeatHandler;
import com.ll.vbc.business.services.server.consensus.follower.handler.FollowerLeaderNotificationHandler;
import com.ll.vbc.business.services.server.consensus.follower.handler.FollowerLogEntryHandler;
import com.ll.vbc.domain.ConsensusServer;
import com.ll.vbc.messageService.request.GeneralRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class LeaderMessageListener implements MessageListener {

    private static final Logger log = LoggerFactory.getLogger(LeaderMessageListener.class);

    private final FollowerHeartbeatHandler followerHeartbeatHandler;
    private final FollowerLogEntryHandler followerLogEntryHandler;
    private final FollowerCommitEntryHandler followerCommitEntryHandler;
    private final FollowerLeaderNotificationHandler followerLeaderNotificationHandler;

    public LeaderMessageListener() {
        followerHeartbeatHandler = new FollowerHeartbeatHandler();
        followerLogEntryHandler = new FollowerLogEntryHandler();
        followerCommitEntryHandler = new FollowerCommitEntryHandler();
        followerLeaderNotificationHandler = new FollowerLeaderNotificationHandler();
        executeCallableHandlers();
    }

    private void executeCallableHandlers() {

        ExecutorService followerLogEntryHandlerTask = Executors.newSingleThreadExecutor();
        Future<?> followerLogEntryHandlerFuture = followerLogEntryHandlerTask.submit(followerLogEntryHandler);

        ExecutorService followerCommitEntryHandlerTask = Executors.newSingleThreadExecutor();
        Future<?> followerCommitEntryHandlerFuture = followerCommitEntryHandlerTask.submit(followerCommitEntryHandler);

    }

    @Override
    public void onMessage(Message message) {
        try {
            GeneralRequest generalRequest = message.getBody(GeneralRequest.class);
            switch (ConsensusServer.getState()) {
                case Follower: {
                    switch(generalRequest.getRequest()) {
                        case Heartbeat: {
                            log.info("onMessage Follower State, request=HeartBeat");
                            followerHeartbeatHandler.handleLeaderHeartbeatRequest(generalRequest);
                            break;
                        }
                        case LeaderLogEntry: {
                            log.info("onMessage Follower State, request=LeaderLogEntry");
                            followerLogEntryHandler.handleLeaderLogEntry(generalRequest);
                            break;
                        }
                        case LeaderLogCommit: {
                            log.info("onMessage Follower State, request=LeaderLogCommit");
                            followerCommitEntryHandler.handleLeaderCommitEntry(generalRequest);
                            break;
                        }
                        case LeaderNotification: {
                            log.info("onMessage Follower State, request=LeaderNotification");
                            followerLeaderNotificationHandler.handleLeaderNotification(generalRequest);
                            break;
                        }
                        default: {
                            log.warn("onMessage: Follower State, Request State: "+generalRequest.getRequest());
                            break;
                        }
                    }
                        break;
                }
                default: {
                    log.warn("onMessage: UnKnown Server State: "+ConsensusServer.getState());
                    break;
                }
            }
        } catch (JMSException e) {
            e.printStackTrace();
        }

    }
}
