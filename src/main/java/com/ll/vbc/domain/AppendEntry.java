package com.ll.vbc.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.Arrays;
import java.util.UUID;

public class AppendEntry implements DomainObject {

    private final Server server;
    private final UUID vToken;
    private final Long index;
    private final Long term;
    private final byte[] electionTransaction;
    private final byte[] blockChainHash;

    public AppendEntry(Server server, UUID vToken, Long index, Long term, byte[] electionTransaction, byte[] blockChainHash) {
        this.server = server;
        this.vToken = vToken;
        this.index = index;
        this.term = term;
        this.electionTransaction = electionTransaction;
        this.blockChainHash = blockChainHash;
    }

    public Server getServer() {
        return server;
    }

    public UUID getvToken() {
        return vToken;
    }

    public Long getIndex() {
        return index;
    }

    public Long getTerm() {
        return term;
    }

    public byte[] getElectionTransaction() {
        return electionTransaction;
    }

    public byte[] getBlockChainHash() {
        return blockChainHash;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        AppendEntry that = (AppendEntry) o;

        return new EqualsBuilder().append(server, that.server).append(vToken, that.vToken).append(index, that.index).append(term, that.term).append(electionTransaction, that.electionTransaction).append(blockChainHash, that.blockChainHash).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(server).append(vToken).append(index).append(term).append(electionTransaction).append(blockChainHash).toHashCode();
    }

    @Override
    public String toString() {
        return "AppendEntry{" +
                "server=" + server +
                ", vToken=" + vToken +
                ", index=" + index +
                ", term=" + term +
                ", electionTransaction=" + Arrays.toString(electionTransaction) +
                ", blockChainHash=" + Arrays.toString(blockChainHash) +
                '}';
    }
}