package com.ll.vbc.domain;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;

public class Election extends Serialization implements Serializable {

    private static final long serialVersionUID = -3740370420901713205L;
    private Date electionDate;
    private String description;

    public Election() {
        electionDate = new Date();
        description = "";
    }
    public Election(Date electionDate, String description) {
        this.electionDate = electionDate;
        this.description = description;
    }

    public Date getElectionDate() {
        return electionDate;
    }

    public String getDescription() {
        return description;
    }

    public byte[] serialize() {
        long longDate = electionDate.getTime();
        byte[] date = serializeLong(longDate);
        byte[] desc = serializeString(description);
        byte[] descLength = serializeInt(desc.length);
        return concatenateBytes(date, desc);

    }

    public int deserialize(byte[] bytes, int ind) {
        if(bytes!=null) {
            electionDate = new Date(deserializeLong(Arrays.copyOfRange(bytes, ind, ind=ind+8)));
            int descLength = deserializeInt(Arrays.copyOfRange(bytes, ind, ind+4));
            description = deserializeString(Arrays.copyOfRange(bytes, ind, ind=ind+descLength+4));
        }
        return ind;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Election election = (Election) o;
        return electionDate.equals(election.electionDate) &&
                description.equals(election.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(electionDate, description);
    }

    @Override
    public String toString() {
        return "Election{" +
                "electionDate=" + electionDate +
                ", description='" + description + '\'' +
                '}';
    }
}
