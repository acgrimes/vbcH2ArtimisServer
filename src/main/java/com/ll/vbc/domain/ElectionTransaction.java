package com.ll.vbc.domain;

import java.io.Serializable;
import java.util.Objects;

public class ElectionTransaction extends Serialization implements Serializable {

    private static final long serialVersionUID = -1464301026352768625L;
    private Voter voter;
    private Election election;
    private Ballot ballot;

    public ElectionTransaction() {
        voter = new Voter();
        election = new Election();
        ballot = new Ballot();
    }
    public ElectionTransaction(Voter voter, Election election, Ballot ballot) {
        this.voter = voter;
        this.election = election;
        this.ballot = ballot;
    }

    public byte[] serialize() {
        byte[] voterBytes = voter.serialize();
        byte[] voterLength = serializeInt(voterBytes.length);
        byte[] electionBytes = election.serialize();
        byte[] electionLength = serializeInt(electionBytes.length);
        byte[] ballotBytes = ballot.serialize();
        byte[] ballotLength = serializeInt(ballotBytes.length);
        return concatenateBytes(voterBytes, electionBytes, ballotBytes);

    }

    public int deserialize(byte[] bytes, int ind) {

        if(bytes!=null) {
            voter = new Voter();
            ind = voter.deserialize(bytes, ind);
            election = new Election();
            ind = election.deserialize(bytes, ind);
            ballot = new Ballot();
            ind = ballot.deserialize(bytes, ind);
        }
        return ind;
    }

    public Voter getVoter() {
        return voter;
    }

    public Election getElection() {
        return election;
    }

    public Ballot getBallot() {
        return ballot;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ElectionTransaction that = (ElectionTransaction) o;
        return voter.equals(that.voter) &&
                election.equals(that.election) &&
                ballot.equals(that.ballot);
    }

    @Override
    public int hashCode() {
        return Objects.hash(voter, election, ballot);
    }

    @Override
    public String toString() {
        return "ElectionTransaction{" +
                "voter=" + voter +
                ", election=" + election +
                ", ballot=" + ballot +
                '}';
    }
}
