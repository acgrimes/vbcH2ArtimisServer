package com.ll.vbc.dao.blockchain;

import com.ll.vbc.dao.blockChain.VoterTokenBlockMapDao;
import com.ll.vbc.domain.VoterTokenBlockMap;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

public class VoterTokenBlockMapTest {

    @Test
    public void saveFindDeleteVoterTokenBlockMapTest()  throws SQLException {

        VoterTokenBlockMap map = new VoterTokenBlockMap(UUID.randomUUID(), 1l);
        VoterTokenBlockMapDao dao = new VoterTokenBlockMapDao();
        try {
            int result = dao.save(map);
            Assert.assertEquals(1, result);

            Optional<VoterTokenBlockMap> mapOp = dao.find(map.getToken());
            Assert.assertEquals(map, mapOp.get());
        } catch(Exception ex) {
            ex.printStackTrace();
        } finally {
            int result = dao.delete(map);
            Assert.assertEquals(1, result);
        }

    }

}
