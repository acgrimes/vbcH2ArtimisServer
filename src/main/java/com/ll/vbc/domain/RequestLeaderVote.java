package com.ll.vbc.domain;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

public class RequestLeaderVote implements DomainObject {

    private String peerHost;
    private int peerPort;
    private AtomicLong logIndex;
    private AtomicLong logTerm;
    private AtomicLong lastLogIndex;
    private AtomicLong lastLogTerm;
    private Boolean grantedVote;

    public RequestLeaderVote() {}
    public RequestLeaderVote(String peerHost, int peerPort, AtomicLong logIndex, AtomicLong logTerm, AtomicLong lastLogIndex, AtomicLong lastLogTerm, Boolean grantedVote) {
        this.peerHost = peerHost;
        this.peerPort = peerPort;
        this.logIndex = logIndex;
        this.logTerm = logTerm;
        this.lastLogIndex = lastLogIndex;
        this.lastLogTerm = lastLogTerm;
        this.grantedVote = grantedVote;
    }

    public String getPeerHost() {
        return peerHost;
    }

    public int getPeerPort() {
        return peerPort;
    }

    public AtomicLong getLogIndex() {
        return logIndex;
    }

    public AtomicLong getLogTerm() {
        return logTerm;
    }

    public AtomicLong getLastLogIndex() {
        return lastLogIndex;
    }

    public AtomicLong getLastLogTerm() {
        return lastLogTerm;
    }

    public Boolean getGrantedVote() {
        return grantedVote;
    }

    public void setGrantedVote(Boolean grantedVote) {
        this.grantedVote = grantedVote;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RequestLeaderVote that = (RequestLeaderVote) o;
        return peerHost.equals(that.peerHost) &&
                peerPort == that.peerPort &&
                logIndex.equals(that.logIndex) &&
                logTerm.equals(that.logTerm) &&
                lastLogIndex.equals(that.lastLogIndex) &&
                lastLogTerm.equals(that.lastLogTerm) &&
                grantedVote.equals(that.grantedVote);
    }

    @Override
    public int hashCode() {
        return Objects.hash(peerHost, peerPort, logIndex, logTerm, lastLogIndex, lastLogTerm, grantedVote);
    }

    @Override
    public String toString() {
        return "RequestLeaderVote{" +
                "peerHost='" + peerHost + '\'' +
                ", peerPort='" + peerPort + '\'' +
                ", logIndex=" + logIndex +
                ", logTerm=" + logTerm +
                ", lastLogIndex=" + lastLogIndex +
                ", lastLogTerm=" + lastLogTerm +
                ", grantedVote=" + grantedVote +
                '}';
    }
}
