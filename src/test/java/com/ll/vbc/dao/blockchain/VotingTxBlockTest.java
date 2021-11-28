package com.ll.vbc.dao.blockchain;

import com.ll.vbc.dao.blockChain.VotingTxBlockDao;
import com.ll.vbc.domain.ElectionTransaction;
import com.ll.vbc.domain.VotingBlockHeader;
import com.ll.vbc.domain.VotingTxBlock;
import com.ll.vbc.utils.BuildElectionTransaction;
import com.ll.vbc.utils.BuildVotingTxBlock;
import org.apache.commons.lang3.SerializationUtils;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class VotingTxBlockTest {

    private static final Logger log = LoggerFactory.getLogger(VotingTxBlockTest.class);


    @Test
    public void saveFindDeleteVotingTxBlock() throws SQLException {

        VotingTxBlockDao votingTxBlockDao = new VotingTxBlockDao();
        ElectionTransaction et = BuildElectionTransaction.build();
        byte[] hash = SerializationUtils.serialize(et);
        VotingTxBlock votingTxBlock = BuildVotingTxBlock.build(11L, 4L, hash);
        try {
            int result = votingTxBlockDao.save(votingTxBlock);
            Assert.assertEquals(1, result);

            Optional<VotingTxBlock> votingTxBlockOp = votingTxBlockDao.find(votingTxBlock.getBlockId());
            votingTxBlockOp.ifPresentOrElse(txBlock -> {
                Map<UUID, ElectionTransaction> txMap = new HashMap<>();
                txBlock.getTransactionMap().forEach((key, value) -> {
                    ElectionTransaction electionTransaction = SerializationUtils.deserialize(value);
                    Assert.assertTrue(votingTxBlock.getTransactionMap().containsKey(key));
                    Assert.assertEquals(et, electionTransaction);
                });
            }, () -> Assert.assertFalse(true));
        } catch(Exception ex) {
            ex.printStackTrace();
        } finally {
            int result = votingTxBlockDao.delete(votingTxBlock);
            Assert.assertEquals(1, result);
        }
    }

    @Test
    public void updateFindDeletVotingTxBlockTest() throws SQLException {

        VotingTxBlockDao votingTxBlockDao = new VotingTxBlockDao();
        ElectionTransaction et = BuildElectionTransaction.build();
        byte[] hash = SerializationUtils.serialize(et);
        VotingTxBlock votingTxBlock = BuildVotingTxBlock.build(11L, 4L, hash);
        int res = votingTxBlockDao.save(votingTxBlock);
        Assert.assertEquals(1, res);

        VotingBlockHeader vbh = new VotingBlockHeader(votingTxBlock.getVotingBlockHeader().getVersion(),
                                                        5L,
                                                        votingTxBlock.getVotingBlockHeader().getPreviousBlockHash(),
                                                        votingTxBlock.getVotingBlockHeader().getMerkleRoot(),
                                                        votingTxBlock.getVotingBlockHeader().getDateTime());
        votingTxBlock.getTransactionMap().put(UUID.randomUUID(), hash);
        VotingTxBlock vtb = new VotingTxBlock(votingTxBlock.getBlockId(), vbh, votingTxBlock.getTransactionMap());
        try {
            int result = votingTxBlockDao.update(vtb);
            Assert.assertEquals(1, result);

            Optional<VotingTxBlock> votingTxBlockOp = votingTxBlockDao.find(votingTxBlock.getBlockId());
            votingTxBlockOp.ifPresentOrElse(txBlock -> {
                txBlock.getTransactionMap().forEach((key, value) -> {
                    ElectionTransaction electionTransaction = SerializationUtils.deserialize(value);
                    Assert.assertTrue(vtb.getTransactionMap().containsKey(key));
                    Assert.assertEquals(et, electionTransaction);
                });
            }, () -> Assert.assertFalse(true));
        } catch(Exception ex) {
            ex.printStackTrace();
        } finally {
            int result = votingTxBlockDao.delete(votingTxBlock);
            Assert.assertEquals(1, result);
        }

    }
}
