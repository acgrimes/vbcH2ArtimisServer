package com.ll.vbc.dao.blockChain;

import com.ll.vbc.dao.Dao;
import com.ll.vbc.domain.VoterTokenBlockMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

public class VoterTokenBlockMapDao extends Dao {

    private static final Logger log = LoggerFactory.getLogger(VoterTokenBlockMapDao.class);
    private static final String saveStatement = "INSERT INTO votertokenblockmap(vtoken, blockid) VALUES (?, ?)";
    private static final String deleteStatement = "DELETE FROM votertokenblockmap WHERE vtoken=?";
    private static final String findStatement = "SELECT vtoken, blockid FROM votertokenblockmap WHERE vtoken=?";

    public VoterTokenBlockMapDao() {
        super();
    }

    public int save(VoterTokenBlockMap voterTokenBlockMap) {

        int result = 0;
        try {
            PreparedStatement statement = connection.prepareStatement(saveStatement);
            statement.setString(1, voterTokenBlockMap.getToken().toString());
            statement.setLong(2, voterTokenBlockMap.getBlockId());
            result = statement.executeUpdate();
        } catch(SQLException ex) {
            ex.printStackTrace();
        }
        return result;
    }

    public Optional<VoterTokenBlockMap> find(UUID uuid) {

        VoterTokenBlockMap voterTokenBlockMap = null;
        try {
            PreparedStatement statement = connection.prepareStatement(findStatement);
            statement.setString(1, uuid.toString());
            ResultSet rs = statement.executeQuery();
            if(rs.next()) {
                UUID uuID = UUID.fromString(rs.getString("vtoken"));
                Long blockId = rs.getLong("blockid");
                voterTokenBlockMap = new VoterTokenBlockMap(uuID, blockId);
            }
        } catch(SQLException ex) {
            ex.printStackTrace();
        }
        return Optional.of(voterTokenBlockMap);
    }

    public int delete(VoterTokenBlockMap voterTokenBlockMap) {

        int result=0;
        try {
            PreparedStatement statement = connection.prepareStatement(deleteStatement);
            statement.setString(1, voterTokenBlockMap.getToken().toString());
            result = statement.executeUpdate();
        } catch(SQLException me) {
            me.printStackTrace();
        }
        return result;
    }
}
