package com.ll.vbc.dao;

import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class DaoTest {

    @Test
    public void H2ConnectionTest() {

        Dao dao = new Dao();

    }

    @Test
    public void createConsensusLogTable() {

        try {
            Dao dao = new Dao();
            Connection connection = dao.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("DROP TABLE IF EXISTS consensusLog");
            preparedStatement.execute();

            Statement statement = connection.createStatement();
            statement.execute("CREATE TABLE consensusLog(logIndex bigint, logTerm bigint, vToken VARCHAR(50), electionTransaction BYTEA)");

            connection.close();
        } catch(SQLException ex) {
            ex.printStackTrace();
        }

    }

    @Test
    public void createMerkleTreeTable() {

        try {
            Dao dao = new Dao();
            Connection connection = dao.getConnection();

            PreparedStatement preparedStatement = connection.prepareStatement("DROP TABLE IF EXISTS merkletree");
            preparedStatement.execute();

            Statement statement = connection.createStatement();
            statement.execute("CREATE TABLE merkletree(blockid bigint, txcount bigint, merkleroot bytea, nodemapkey ARRAY, nodemapvalue ARRAY)");

            connection.close();
        } catch(SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void createTokenBlockMapTable() {

        try {
            Dao dao = new Dao();
            Connection connection = dao.getConnection();

            PreparedStatement preparedStatement = connection.prepareStatement("DROP TABLE IF EXISTS votertokenblockmap");
            preparedStatement.execute();

            Statement statement = connection.createStatement();
            statement.execute("CREATE TABLE votertokenblockmap(vtoken varchar, blockid bigint)");

            connection.close();
        } catch(SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void createVotingTxBlockTable() {

        try {
            Dao dao = new Dao();
            Connection connection = dao.getConnection();

            PreparedStatement preparedStatement = connection.prepareStatement("DROP TABLE IF EXISTS votingTxBlock");
            preparedStatement.execute();

            Statement statement = connection.createStatement();
            statement.execute("CREATE TABLE votingTxBlock(blockid bigint, major integer, minor integer, txcount bigint, previousblockHash bytea, merkleroot bytea, datetime date, txmapkey array, txmapvalue array)");

            connection.close();
        } catch(SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void truncateAllTables() {

        try {
            Dao dao = new Dao();
            Connection connection = dao.getConnection();
            Statement statement = connection.createStatement();
            statement.execute("TRUNCATE TABLE CONSENSUSLOG");
            statement.execute("TRUNCATE TABLE VOTERTOKENBLOCKMAP");
            statement.execute("TRUNCATE TABLE VOTINGTXBLOCK");
            statement.execute("TRUNCATE TABLE MERKLETREE");

            connection.close();
        } catch(SQLException ex) {
            ex.printStackTrace();
        }

    }
}
