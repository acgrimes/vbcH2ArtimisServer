package com.ll.vbc.dao.blockChain;

import com.ll.vbc.dao.Dao;
import com.ll.vbc.domain.Version;
import com.ll.vbc.domain.VotingBlockHeader;
import com.ll.vbc.domain.VotingTxBlock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class VotingTxBlockDao extends Dao {

    private static final Logger log = LoggerFactory.getLogger(MerkleTreeDao.class);
    private static final String saveStatement = "INSERT INTO votingtxblock (blockid, major, minor, txcount, previousblockhash, merkleroot, datetime, txmapkey, txmapvalue) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String updateStatement = "UPDATE votingtxblock SET txcount=?, merkleroot=?, txmapkey=?, txmapvalue=? WHERE blockid=?";
    private static final String deleteStatement = "DELETE FROM votingtxblock WHERE blockid=?";
    private static final String findStatement = "SELECT blockid, major, minor, txcount, previousblockhash, merkleroot, datetime, txmapkey, txmapvalue FROM votingtxblock WHERE blockid=?";

    public VotingTxBlockDao() {
        super();
    }

    public int save(VotingTxBlock votingTxBlock) {

        int result = 0;
        try {
            MapArray mapArray = convertMapToSqlArray(votingTxBlock.getTransactionMap());
            PreparedStatement statement = connection.prepareStatement(saveStatement);
            statement.setLong(1, votingTxBlock.getBlockId());
            statement.setInt(2, votingTxBlock.getVotingBlockHeader().getVersion().getMajor());
            statement.setInt(3, votingTxBlock.getVotingBlockHeader().getVersion().getMinor());
            statement.setLong(4, votingTxBlock.getVotingBlockHeader().getTxCount());
            statement.setBytes(5, votingTxBlock.getVotingBlockHeader().getPreviousBlockHash());
            statement.setBytes(6, votingTxBlock.getVotingBlockHeader().getMerkleRoot());
            java.sql.Date sqlDate = new java.sql.Date(votingTxBlock.getVotingBlockHeader().getDateTime().getTime());
            statement.setDate(7, sqlDate);
            statement.setArray(8, mapArray.getKeyArray());
            statement.setArray(9, mapArray.getValueArray());
            result = statement.executeUpdate();
        } catch(SQLException ex) {
            ex.printStackTrace();
        }
        return result;

    }

    public int update(VotingTxBlock votingTxBlock) {

        int result = 0;
        try {
            MapArray mapArray = convertMapToSqlArray(votingTxBlock.getTransactionMap());
            PreparedStatement statement = connection.prepareStatement(updateStatement);
            statement.setLong(1, votingTxBlock.getVotingBlockHeader().getTxCount());
            statement.setBytes(2, votingTxBlock.getVotingBlockHeader().getMerkleRoot());
            statement.setArray(3, mapArray.getKeyArray());
            statement.setArray(4, mapArray.getValueArray());
            statement.setLong(5, votingTxBlock.getBlockId());
            result = statement.executeUpdate();
        } catch(SQLException ex) {
            ex.printStackTrace();
        }
        return result;

    }

    /**
     *
     * @param id
     * @return
     */
    public Optional<VotingTxBlock> find(final Long id) {

        VotingTxBlock votingTxBlock = null;
        try {
            PreparedStatement statement = connection.prepareStatement(findStatement);
            statement.setLong(1, id);
            ResultSet rs = statement.executeQuery();
            if(rs.next()) {
                Long blockid = rs.getLong("blockid");
                Integer major = rs.getInt("major");
                Integer minor = rs.getInt("minor");
                Long txCount = rs.getLong("txcount");
                byte[] previousBlockHash = rs.getBytes("previousblockhash");
                byte[] merkleRoot = rs.getBytes("merkleroot");
                Date dateTime = rs.getDate("datetime");
                String[] nodeMapKey = (String[]) rs.getArray("txmapkey").getArray();
                byte[][] nodeMapValue = (byte[][]) rs.getArray("txmapvalue").getArray();
                Map<UUID, byte[]> transactionMap = new HashMap<>();
                for(int i=0;i<nodeMapKey.length;i++) {
                    log.debug("find - nodeMapKey: "+nodeMapKey[i]+", nodeMapValue: "+ Arrays.toString(nodeMapValue[i]));
                    transactionMap.put(UUID.fromString(nodeMapKey[i]), nodeMapValue[i]);
                }
                Version version = new Version(major, minor);
                VotingBlockHeader votingBlockHeader = new VotingBlockHeader(version, txCount, previousBlockHash, merkleRoot, dateTime);
                votingTxBlock = new VotingTxBlock(blockid, votingBlockHeader, transactionMap);
            }
        } catch(SQLException ex) {
            ex.printStackTrace();
        }
        return Optional.of(votingTxBlock);
    }

    /**
     *
     * @param votingTxBlock
     * @return
     */
    public int delete(final VotingTxBlock votingTxBlock) {

        int result=0;
        try {
            PreparedStatement statement = connection.prepareStatement(deleteStatement);
            statement.setLong(1, votingTxBlock.getBlockId());
            result = statement.executeUpdate();
        } catch(SQLException me) {
            me.printStackTrace();
        }
        return result;
    }
}
