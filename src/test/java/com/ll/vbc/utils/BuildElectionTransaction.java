package com.ll.vbc.utils;

import com.ll.vbc.domain.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BuildElectionTransaction {

    public static ElectionTransaction build() {

        VotingDistrict votingDistrict = new VotingDistrict(1,1,1,"",1,1,1);
        UUID voterToken = UUID.randomUUID();
        Voter voter = new Voter(2L, voterToken, votingDistrict);
        Map<String, String> office = new HashMap<>();
        office.put("president", "Donald Trump");
        Map<String, String> question = new HashMap<>();
        question.put("article1", "Yes");

        Ballot ballot = new Ballot(voterToken, office, question);

        Election election = new Election(new Date(), "Georgia");

        ElectionTransaction electionTransaction = new ElectionTransaction(voter, election, ballot);

        return electionTransaction;
    }


}
