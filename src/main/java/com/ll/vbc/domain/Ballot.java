package com.ll.vbc.domain;

import java.io.Serializable;
import java.util.*;

public class Ballot extends Serialization implements Serializable {

    private static final long serialVersionUID = -6919637677219192555L;
    private UUID vToken;
    private Map<String, String> officeCandidate;
    private Map<String, String> questionAnswer;

    public Ballot() {
        vToken = UUID.randomUUID();
        officeCandidate = new HashMap<>();
        questionAnswer = new HashMap<>();
    }
    public Ballot(UUID vToken, Map<String, String> officeCandidate, Map<String, String> questionAnswer) {
        this.vToken = vToken;
        this.officeCandidate = officeCandidate;
        this.questionAnswer = questionAnswer;
    }

    public UUID getvToken() {
        return vToken;
    }

    public Map<String, String> getOfficeCandidate() {
        return officeCandidate;
    }

    public Map<String, String> getQuestionAnswer() {
        return questionAnswer;
    }

    public final byte[] serialize() {
        byte[] uuidBytes = serializeString(vToken.toString());
        byte[] uuidLength = serializeInt(uuidBytes.length);
        byte[] officeCandidateBytes = serializeMap(officeCandidate);
        byte[] officeCandidateLength = serializeInt(officeCandidateBytes.length);
        byte[] questionAnswerBytes = serializeMap(questionAnswer);
        byte[] questionAnswerLength = serializeInt(questionAnswerBytes.length);
        return concatenateBytes(uuidBytes, officeCandidateLength, officeCandidateBytes, questionAnswerLength, questionAnswerBytes);
    }

    public final int deserialize(byte[] bytes, int index) {
        if(bytes!=null) {
            if (index < bytes.length) {
                int vTokenLength = deserializeInt(Arrays.copyOfRange(bytes, index, index + 4));
                vToken = UUID.fromString(deserializeString(Arrays.copyOfRange(bytes, index, index = index+vTokenLength+4)));
            }
            if (index < bytes.length) {
                int mapLength = deserializeInt(Arrays.copyOfRange(bytes, index, index=index + 4));
                officeCandidate = deserializeMap(Arrays.copyOfRange(bytes, index, index = index + mapLength));
            }
            if (index < bytes.length) {
                int mapLength = deserializeInt(Arrays.copyOfRange(bytes, index, index=index + 4));
                questionAnswer = deserializeMap(Arrays.copyOfRange(bytes, index, index=index + mapLength));
            }
        }
        return index;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ballot ballot = (Ballot) o;
        return vToken.equals(ballot.vToken) &&
                officeCandidate.equals(ballot.officeCandidate) &&
                questionAnswer.equals(ballot.questionAnswer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(vToken, officeCandidate, questionAnswer);
    }

    @Override
    public String toString() {
        return "Ballot{" +
                "vToken=" + vToken +
                ", officeCandidate=" + officeCandidate +
                ", questionAnswer=" + questionAnswer +
                '}';
    }
}
