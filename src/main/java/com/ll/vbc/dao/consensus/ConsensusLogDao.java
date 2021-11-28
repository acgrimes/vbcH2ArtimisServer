package com.ll.vbc.dao.consensus;

import com.ll.vbc.dao.Dao;
import com.ll.vbc.domain.AppendEntry;
import com.ll.vbc.domain.ConsensusLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

import static com.ll.vbc.domain.ConsensusLog.Variables.logIndex;

public class ConsensusLogDao extends Dao {

    private static final Logger log = LoggerFactory.getLogger(ConsensusLogDao.class);
    private static String saveStatement = "INSERT INTO consensusLog (logIndex, logTerm, vToken, electionTransaction)" +
                                            "VALUES (?, ?, ?, ?)";
    private static String deleteStatement = "DELETE FROM consensusLog WHERE logIndex=?";
    private static String findStatement = "SELECT logIndex, logTerm, vToken, electionTransaction FROM consensusLog WHERE logIndex=?";

    public ConsensusLogDao() {
        super();
    }

    /**
     *
     * @param index
     * @return
     */
    public Optional<ConsensusLog> find(final Long index) {

        ConsensusLog consensusLog = null;
        try {
            PreparedStatement statement = connection.prepareStatement(findStatement);
            statement.setLong(1, index);
            ResultSet rs = statement.executeQuery();
            if(rs.next()) {
                consensusLog = new ConsensusLog(rs.getLong(logIndex.name()),
                        rs.getLong("logTerm"),
                        UUID.fromString(rs.getString("vToken")),
                        rs.getBytes("electionTransaction"));
            }
        } catch(SQLException ex) {
            ex.printStackTrace();
        }
        return Optional.of(consensusLog);
    }

    public int delete(final ConsensusLog consensusLog) {
        int result=0;
        try {
            PreparedStatement statement = connection.prepareStatement(deleteStatement);
            statement.setLong(1, consensusLog.getLogIndex());
            result = statement.executeUpdate();
        } catch(SQLException me) {
            me.printStackTrace();
        }
        return result;
    }

    public int save(final ConsensusLog consensusLog) {

        log.debug("save: "+consensusLog.toString());

        int result=0;
        try {
            PreparedStatement statement = connection.prepareStatement(saveStatement);
            statement.setLong(1, consensusLog.getLogIndex());
            statement.setLong(2, consensusLog.getLogTerm());
            statement.setString(3, consensusLog.getvToken().toString());
            statement.setBytes(4, consensusLog.getElectionTransaction());
            result = statement.executeUpdate();
        } catch(SQLException me) {
            me.printStackTrace();
            return result;
        }
        return result;
    }

    public int save(final Long index, final Long term, final UUID vToken, final byte[] electionTransaction) {
        log.debug("save: ");

        int result=0;
        try {
            PreparedStatement statement = connection.prepareStatement(saveStatement);
            statement.setLong(1, index);
            statement.setLong(2, term);
            statement.setString(3, vToken.toString());
            statement.setBytes(4, electionTransaction);
            result = statement.executeUpdate();
        } catch(SQLException me) {
            me.printStackTrace();
            return result;
        }
        return result;
    }

    public int save(final AppendEntry appendEntry) {
        log.debug("save: "+appendEntry.toString());

        int result=0;
        try {
            PreparedStatement statement = connection.prepareStatement(saveStatement);
            statement.setLong(1, appendEntry.getIndex());
            statement.setLong(2, appendEntry.getTerm());
            statement.setString(3, appendEntry.getvToken().toString());
            statement.setBytes(4, appendEntry.getElectionTransaction());
            result = statement.executeUpdate();
        } catch(SQLException me) {
            me.printStackTrace();
            return result;
        }
        return result;
    }
}
