package com.ll.vbc.business.services.client.consensus.leader.request;

import com.ll.vbc.domain.AppendEntry;
import com.ll.vbc.domain.ConsensusServer;
import com.ll.vbc.domain.ConsensusState;
import com.ll.vbc.enums.Request;
import com.ll.vbc.messageService.artemis.JmsManager;
import com.ll.vbc.messageService.request.GeneralRequest;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.apache.commons.lang3.SerializationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LeaderHeartbeatRequest {

    private static final Logger log = LoggerFactory.getLogger(LeaderHeartbeatRequest.class);

    private final JmsManager jmsManager;

    public LeaderHeartbeatRequest() {
        jmsManager = JmsManager.getInstance();
    }

    /**
     *  This method builds the Leader Heartbeat message sent to all follower servers:
     */
    public void sendLeaderHeartbeatRequest() {

        AppendEntry appendEntry = new AppendEntry(ConsensusServer.getServerInstance(),
                                                null,
                                                ConsensusState.getCurrentIndex().get(),
                                                ConsensusState.getCurrentTerm().get(),
                                                null, null);
        GeneralRequest generalRequest = new GeneralRequest(Request.Heartbeat,
                                                            appendEntry,null, null);

        jmsManager.sendLeaderMessage(generalRequest);
    }
}
