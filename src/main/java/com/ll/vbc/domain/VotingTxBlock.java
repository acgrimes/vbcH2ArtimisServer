package com.ll.vbc.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public class VotingTxBlock implements DomainObject {

    public enum Variables {
            votingTxBlock,
            blockId,
            votingBlockHeader,
            transactionMap }

    private Long blockId;
    private VotingBlockHeader votingBlockHeader;

    // maps from voter token (UUID) to the election transaction
    private Map<UUID, byte[]> transactionMap = new HashMap<>();

    public VotingTxBlock() {}
    public VotingTxBlock(Long blockId, VotingBlockHeader votingBlockHeader, Map<UUID, byte[]> transactionMap) {
        this.blockId = blockId;
        this.votingBlockHeader = votingBlockHeader;
        this.transactionMap = transactionMap;
    }

    public Long getBlockId() {
        return blockId;
    }

    public void setBlockId(Long blockId) {
        this.blockId = blockId;
    }

    public VotingBlockHeader getVotingBlockHeader() {
        return votingBlockHeader;
    }

    public void setVotingBlockHeader(VotingBlockHeader votingBlockHeader) {
        this.votingBlockHeader = votingBlockHeader;
    }

    public Map<UUID, byte[]> getTransactionMap() {
        return transactionMap;
    }

    public void setTransactionMap(Map<UUID, byte[]> transactionMap) {
        this.transactionMap = transactionMap;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        VotingTxBlock that = (VotingTxBlock) o;

        return new EqualsBuilder()
                .append(blockId, that.blockId)
                .append(votingBlockHeader, that.votingBlockHeader)
                .append(transactionMap, that.transactionMap)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(blockId)
                .append(votingBlockHeader)
                .append(transactionMap)
                .toHashCode();
    }

    @Override
    public String toString() {
        return "VotingTxBlock{" +
                "blockId=" + blockId +
                ", votingBlockHeader=" + votingBlockHeader +
                ", transactionMap=" + transactionMap +
                '}';
    }
}
