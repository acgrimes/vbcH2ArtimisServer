package com.ll.vbc.utils;


import com.ll.vbc.domain.MerkleTree;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BuildMerkleTree {

    private static final Logger log = LoggerFactory.getLogger(BuildMerkleTree.class);

    public static MerkleTree build(Long blockId, Long txCount, byte[] hash) {

        Map<UUID, byte[]> nodeMap = new HashMap<>();
        for(long i=0; i<txCount; i++) {
            nodeMap.put(UUID.randomUUID(), hash);
            log.debug("hash: "+ Arrays.toString(hash));
        }
        MerkleTree merkleTree = new MerkleTree(blockId, txCount, hash, nodeMap);
        return merkleTree;
    }

}
