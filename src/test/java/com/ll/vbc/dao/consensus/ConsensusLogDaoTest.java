package com.ll.vbc.dao.consensus;

import com.ll.vbc.dao.Dao;
import com.ll.vbc.domain.ConsensusLog;
import com.ll.vbc.domain.ElectionTransaction;
import com.ll.vbc.utils.BuildElectionTransaction;
import org.apache.commons.lang3.SerializationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

public class ConsensusLogDaoTest {

    private static final Logger log = LoggerFactory.getLogger(Dao.class);

    @Test
    public void saveConsensusLogTest() {

        ElectionTransaction et = BuildElectionTransaction.build();
        ConsensusLog cl = new ConsensusLog(2L, 2L, UUID.randomUUID(), SerializationUtils.serialize(et));
        ConsensusLogDao dao = new ConsensusLogDao();
        int result = dao.save(cl);
        Assert.assertEquals(1, result);

        result = dao.delete(cl);
        Assert.assertEquals(1, result);
    }

    @Test
    public void saveFindDeleteConsensusLogTest() {

        ElectionTransaction et = BuildElectionTransaction.build();
        ConsensusLog cl = new ConsensusLog(2L, 2L, UUID.randomUUID(), SerializationUtils.serialize(et));
        ConsensusLogDao dao = new ConsensusLogDao();
        try {
            int result = dao.save(cl);
            Assert.assertEquals(1, result);

            Optional<ConsensusLog> clOp = dao.find(cl.getLogIndex());
            Assert.assertEquals(cl, clOp.get());
        } catch(Exception ex) {
            ex.printStackTrace();
        } finally {
            int result = dao.delete(cl);
            Assert.assertEquals(1, result);
        }
    }
}
