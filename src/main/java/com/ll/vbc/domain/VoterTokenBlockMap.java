package com.ll.vbc.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.UUID;

/**
 * This class maps a voter token identifier, that is the voter ballot summary, with the
 * block id of the block in the blockchain that holds the voters ballot summary.
 */
public class VoterTokenBlockMap implements DomainObject {

    public VoterTokenBlockMap() {}
    public VoterTokenBlockMap(UUID token, Long blockId) {
        this.token = token;
        this.blockId = blockId;
    }

    private UUID token;
    private Long blockId;

    public UUID getToken() {
        return token;
    }

    public void setToken(UUID token) {
        this.token = token;
    }

    public Long getBlockId() {
        return blockId;
    }

    public void setBlockId(Long blockId) {
        this.blockId = blockId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        VoterTokenBlockMap that = (VoterTokenBlockMap) o;

        return new EqualsBuilder()
                .append(token, that.token)
                .append(blockId, that.blockId)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(token)
                .append(blockId)
                .toHashCode();
    }

    @Override
    public String toString() {
        return "VoterTokenBlockMap{" +
                "token=" + token +
                ", blockId=" + blockId +
                '}';
    }
}
