package com.ll.vbc.domain;


import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.Arrays;
import java.util.UUID;

public class ConsensusLog implements DomainObject, Comparable {

    public enum Variables {
        consensusLog,
        logIndex,
        logTerm,
        vToken,
        electionTransaction
    }

    private Long logIndex;
    private Long logTerm;
    private UUID vToken;
    private byte[] electionTransaction;

    public ConsensusLog(Long logIndex, Long logTerm, UUID vToken, byte[] electionTransaction) {
        this.logIndex = logIndex;
        this.logTerm = logTerm;
        this.vToken = vToken;
        this.electionTransaction = electionTransaction;
    }

    public Long getLogIndex() {
        return logIndex;
    }

    public Long getLogTerm() {
        return logTerm;
    }

    public UUID getvToken() {
        return vToken;
    }

    public byte[] getElectionTransaction() {
        return electionTransaction;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        ConsensusLog that = (ConsensusLog) o;

        return new EqualsBuilder()
                .append(logIndex, that.logIndex)
                .append(logTerm, that.logTerm)
                .append(vToken, that.vToken)
                .append(electionTransaction, that.electionTransaction)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(logIndex)
                .append(logTerm)
                .append(vToken)
                .append(electionTransaction)
                .toHashCode();
    }

    public int compareTo(Object o) {
        ConsensusLog myClass = (ConsensusLog) o;
        return new CompareToBuilder()
            .append(this.logIndex, myClass.logIndex)
            .append(this.logTerm, myClass.logTerm)
            .append(this.vToken, myClass.vToken)
            .append(this.electionTransaction, myClass.electionTransaction)
            .toComparison();
    }

    @Override
    public String toString() {
        return "ConsensusLog{" +
                "logIndex=" + logIndex +
                ", logTerm=" + logTerm +
                ", vToken=" + vToken +
                ", electionTransaction=" + Arrays.toString(electionTransaction) +
                '}';
    }

    public boolean validConsensusLog() {
        if(logIndex==null&&logIndex<1) {
            return false;
        }
        if(logTerm==null&&logTerm<1) {
            return false;
        }
        if(vToken==null) {
            return false;
        }
        if(electionTransaction==null&&electionTransaction.length==0) {
            return false;
        }
        return true;
    }
}
