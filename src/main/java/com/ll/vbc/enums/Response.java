package com.ll.vbc.enums;

import com.ll.vbc.domain.Serialization;

import java.io.Serializable;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum Response implements Serializable {
    Authorization,
    Authentication,
    CompletedBallot,
    Ballot,
    BallotAccepted,
    BallotInvalid,
    LogEntry,
    LogCommit,
    Last;

    private static final Map<Integer, Response> lookup = new HashMap<Integer, Response>();

    static{
        int ordinal = 0;
        for (Response response : EnumSet.allOf(Response.class)) {
            lookup.put(ordinal, response);
            ordinal+= 1;
        }
    }

    public final static Response fromOrdinal(int ordinal) {
        return lookup.get(ordinal);
    }

    public byte[] serialize() {
        Serialization serial = new Serialization();
        byte[] responseBytes = serial.serializeInt(ordinal());
        return responseBytes;
    }

    public static Response deserialize(byte[] response) {
        Serialization serial = new Serialization();
        int enumValue = serial.deserializeInt(response);
        return Response.fromOrdinal(enumValue);
    }
}
