package com.ll.vbc.dao.blockchain;

import com.ll.vbc.dao.blockChain.MerkleTreeDao;
import com.ll.vbc.domain.ElectionTransaction;
import com.ll.vbc.domain.MerkleTree;
import com.ll.vbc.merkle.Merkle;
import com.ll.vbc.utils.BuildElectionTransaction;
import com.ll.vbc.utils.BuildMerkleTree;
import org.apache.commons.lang3.SerializationUtils;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class MerkleTreeTest {
    private static final Logger log = LoggerFactory.getLogger(MerkleTreeTest.class);
    private final MerkleTreeDao merkleTreeDao = new MerkleTreeDao();

    @Test
    public void saveDeleteMerkleTree() {

        ElectionTransaction et = BuildElectionTransaction.build();
        byte[] hash = SerializationUtils.serialize(et);
        log.debug("saveDeleteMerkleTree - hash: "+ Arrays.toString(hash));
        MerkleTree merkleTree = BuildMerkleTree.build(5L, 4L, hash);
        boolean result = merkleTreeDao.save(merkleTree);
        Assert.assertEquals(true, result);

//        result = merkleTreeDao.delete(merkleTree);
//        Assert.assertEquals(1, result);
    }

    @Test
    public void insertDeleteMerkleTree() {

        ElectionTransaction et = BuildElectionTransaction.build();
        byte[] hash = SerializationUtils.serialize(et);
        log.debug("saveDeleteMerkleTree - hash: "+ Arrays.toString(hash));
        MerkleTree merkleTree = BuildMerkleTree.build(3L, 4L, hash);
        int result = merkleTreeDao.insert(merkleTree);
        Assert.assertEquals(1, result);

        Optional<MerkleTree> merkleTreeOp = merkleTreeDao.find(merkleTree.getBlockId());
        if(merkleTreeOp.isPresent()) {
            Set<Map.Entry<UUID, byte[]>> mtSet = merkleTree.getNodeMap().entrySet();
            Set<Map.Entry<UUID, byte[]>> opSet = merkleTreeOp.get().getNodeMap().entrySet();
            mtSet.stream().forEach(mt -> log.debug("insertDeleteMerkleTree - mt key: "+mt.getKey().toString()+", value: "+Arrays.toString(mt.getValue())));
            opSet.stream().forEach(op -> log.debug("insertDeleteMerkleTree - op key: "+op.getKey().toString()+", value: "+Arrays.toString(op.getValue())));
        }

        int deleteResult = merkleTreeDao.delete(merkleTree);
        Assert.assertEquals(1, deleteResult);

        Optional<MerkleTree> findMerkleTree = merkleTreeDao.find(merkleTree.getBlockId());
        Assert.assertFalse(findMerkleTree.isPresent());

    }
}
