package com.ll.vbc.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.Arrays;
import java.util.Map;
import java.util.UUID;


public final class MerkleTree implements DomainObject {

    private Long blockId;
    private Long txCount;
    private byte[] merkleRoot;
    private Map<UUID, byte[]> nodeMap;

    public MerkleTree() {}
    public MerkleTree(Long blockId, Long txCount, byte[] merkleRoot, Map<UUID, byte[]> nodeMap) {
        this.blockId = blockId;
        this.txCount = txCount;
        this.merkleRoot = merkleRoot;
        this.nodeMap = nodeMap;
    }

    public Long getBlockId() {
        return blockId;
    }

    public void setBlockId(Long blockId) {
        this.blockId = blockId;
    }

    public Long getTxCount() {
        return txCount;
    }

    public void setTxCount(Long txCount) {
        this.txCount = txCount;
    }

    public byte[] getMerkleRoot() {
        return merkleRoot;
    }

    public void setMerkleRoot(byte[] merkleRoot) {
        this.merkleRoot = merkleRoot;
    }

    public Map<UUID, byte[]> getNodeMap() {
        return nodeMap;
    }

    public void setNodeMap(Map<UUID, byte[]> nodeMap) {
        this.nodeMap = nodeMap;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        MerkleTree that = (MerkleTree) o;

        return new EqualsBuilder()
                .append(blockId, that.blockId)
                .append(txCount, that.txCount)
                .append(merkleRoot, that.merkleRoot)
                .append(nodeMap, that.nodeMap)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(blockId)
                .append(txCount)
                .append(merkleRoot)
                .append(nodeMap)
                .toHashCode();
    }

    @Override
    public String toString() {
        return "MerkleTree{" +
                "blockId=" + blockId +
                ", txCount=" + txCount +
                ", merkleRoot=" + Arrays.toString(merkleRoot) +
                ", nodeMap=" + nodeMap +
                '}';
    }
}
