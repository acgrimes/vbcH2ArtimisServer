package com.ll.vbc.utils;


import com.ll.vbc.domain.Version;
import com.ll.vbc.domain.VotingBlockHeader;
import com.ll.vbc.domain.VotingTxBlock;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BuildVotingTxBlock {

    public static VotingTxBlock build(Long blockId, Long txCount, byte[] hash) {

        Version version = new Version(1, 2);
        VotingBlockHeader header = new VotingBlockHeader(version, txCount, hash, hash, new Date());
        Map<UUID, byte[]> txMap = new HashMap<>();
        for(int i=0;i<txCount;i++) {
            UUID id = UUID.randomUUID();
            txMap.put(id, hash);
        }
        return new VotingTxBlock(blockId, header, txMap);
    }
}
