package com.ll.vbc.domain;

import java.util.Objects;
import java.util.UUID;

public class ValidElectionTx implements DomainObject {

    private UUID vtoken;
    private Long logIndex;
    private byte[] electionTransaction;

    public ValidElectionTx(UUID vtoken, Long logIndex, byte[] electionTransaction) {
        this.vtoken = vtoken;
        this.logIndex = logIndex;
        this.electionTransaction = electionTransaction;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ValidElectionTx that = (ValidElectionTx) o;
        return vtoken.equals(that.vtoken) &&
                logIndex.equals(that.logIndex);
    }

    @Override
    public int hashCode() {
        return Objects.hash(vtoken, logIndex);
    }

    public UUID getVtoken() {
        return vtoken;
    }

    public Long getLogIndex() {
        return logIndex;
    }

    public byte[] getElectionTransaction() {
        return electionTransaction;
    }
}
