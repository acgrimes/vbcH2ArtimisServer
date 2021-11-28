package com.ll.vbc.enums;

import java.io.Serializable;

public enum ServerConsensusState implements Serializable {

    Follower,
    Candidate,
    Leader,
    Proxy,
    NonState;

    static {
        state = Follower;
    }

    private static ServerConsensusState state;

    public static ServerConsensusState getState() {
        return state;
    }

    public static void setState(ServerConsensusState state) {
        state = state;
    }
}
