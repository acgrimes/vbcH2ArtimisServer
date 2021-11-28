package com.ll.vbc.enums;


import com.ll.vbc.domain.Serialization;

import java.io.Serializable;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import static com.ll.vbc.utils.SerialUtil.byteArrayToInt;

public enum Request implements Serializable {
    Login,
    Heartbeat,
    HeartbeatFollowerResponse,
    Authentication,
    BallotRequest,
    ElectionTransaction,
    ViewBallot,
    RequestLeaderVote,
    FollowerGrantsLeaderVote,
    FollowerDeniesLeaderVote,
    CommittedBallot,
    LeaderNotification,
    Ballot,
    LeaderLogEntry,
    FollowerLogEntry,
    LeaderLogCommit,
    FollowerLogCommit,
    FollowerLogEntryFailure,
    FollowerCommitEntryFailure,
    Last;

    private static final Map<Integer, Request> lookup = new HashMap<Integer, Request>();

    static{
        int ordinal = 0;
        for (Request request : EnumSet.allOf(Request.class)) {
            lookup.put(ordinal, request);
            ordinal+= 1;
        }
    }

    public static Request fromOrdinal(int ordinal) {
        return lookup.get(ordinal);
    }

    private static Request setRequest(byte[] message) {

        byte[] requestTypeLength = new byte[4];
        for(int i=0; i<4; i++) {
            requestTypeLength[i] = message[i];
        }
        int ord = byteArrayToInt(requestTypeLength);
        return Request.fromOrdinal(ord);
    }

    public byte[] serialize() {
        Serialization serial = new Serialization();
        byte[] requestByte = serial.serializeInt(this.ordinal());
        return requestByte;
    }

    public static Request deserialize(byte[] request) {
        Serialization serial = new Serialization();
        int enumValue = serial.deserializeInt(request);
        return Request.fromOrdinal(enumValue);
    }
}
