package com.ll.vbc.dao.blockChain;

import com.ll.vbc.dao.Dao;
import com.ll.vbc.domain.MerkleTree;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;


public class MerkleTreeDao extends Dao {

    private static final Logger log = LoggerFactory.getLogger(MerkleTreeDao.class);
    private static String saveStatement = "INSERT INTO merkletree (blockid, txcount, merkleroot, nodemapkey, nodemapvalue) VALUES (?, ?, ?, ?, ?)";
    private static String deleteStatement = "DELETE FROM merkletree WHERE blockid=?";
    private static String findStatement = "SELECT blockid, txcount, merkleroot, nodemapkey, nodemapvalue FROM merkletree WHERE blockid=?";

    private static class NodeMap {
        public String uuid;
        public byte[] node;
        public NodeMap() {}
        public NodeMap(String uuid, byte[] node) {
            this.uuid = uuid;
            this.node = node;
        }
    }

    public MerkleTreeDao() {
        super();
    }


    public Optional<MerkleTree> find(final Long id) {

        MerkleTree merkleTree = null;
        try {
            PreparedStatement statement = connection.prepareStatement(findStatement);
            statement.setLong(1, id);
            ResultSet rs = statement.executeQuery();
            if(rs.next()) {
                Long blockid = rs.getLong("blockid");
                Long txCount = rs.getLong("txcount");
                byte[] merkleRoot = rs.getBytes("merkleroot");
                String[] nodeMapKey = (String[]) rs.getArray("nodemapkey").getArray();
                byte[][] nodeMapValue = (byte[][]) rs.getArray("nodemapvalue").getArray();
                Map<UUID, byte[]> nodeMap = new HashMap<>();
                for(int i=0;i<nodeMapKey.length;i++) {
                    log.debug("find - nodeMapKey: "+nodeMapKey[i]+", nodeMapValue: "+Arrays.toString(nodeMapValue[i]));
                    nodeMap.put(UUID.fromString(nodeMapKey[i]), nodeMapValue[i]);
                }
                merkleTree = new MerkleTree(blockid, txCount, merkleRoot, nodeMap);
            }
        } catch(SQLException ex) {
            ex.printStackTrace();
        }
        return Optional.ofNullable(merkleTree);
    }

    /**
     * delete a MerkleTree
     * @param merkleTree
     * @return
     */
    public int delete(final MerkleTree merkleTree) {
        int result=0;
        try {
            PreparedStatement statement = connection.prepareStatement(deleteStatement);
            statement.setLong(1, merkleTree.getBlockId());
            result = statement.executeUpdate();
        } catch(SQLException me) {
            me.printStackTrace();
        }
        return result;
    }

    /**
     * update MerkleTree
     * @param merkleTree
     * @return
     */
    public int update(final MerkleTree merkleTree) {

        int result=0;

        return result;

    }

    public boolean save(final MerkleTree merkleTree) {

        log.debug("save: "+merkleTree.toString());

        final List<NodeMap> nodeMapList = new ArrayList<>();
        merkleTree.getNodeMap().forEach((key, value) -> {
            log.debug("key = "+key+", value = "+Arrays.toString(value));
            nodeMapList.add(new NodeMap(key.toString(), value));
        });
        NodeMap[] nodeMaps = nodeMapList.toArray(new NodeMap[nodeMapList.size()]);
        String[] nodeMapKey = new String[nodeMapList.size()];
        byte[][] nodeMapValue = new byte[nodeMapList.size()][256];

        int i = 0;
        for(NodeMap nodeMap : nodeMapList) {
            nodeMapKey[i] = nodeMap.uuid;
            nodeMapValue[i++] = nodeMap.node;
        }

        boolean result=false;
        try {
//            Map connMap = connection.getTypeMap();
//            connMap.put("map", new com.ll.vbc.dao.blockChain.MerkleTreeDao.NodeMap());
//            connection.setTypeMap(connMap);
//            Array array = connection.createArrayOf("map", nodeMaps);
//            PreparedStatement statement = connection.prepareStatement(saveStatement);
            Statement statement = connection.createStatement();
//            String saveStatement = "INSERT INTO merkletree VALUES ("+merkleTree.getBlockId()+", "+merkleTree.getTxCount()+", CAST('"+merkleTree.getMerkleRoot()+"' AS bytea), CAST(ARRAY[[ROW('Hello', CAST('\\047' AS bytea))]] AS map[]))";
            String saveStatement = "INSERT INTO merkletree VALUES ("+merkleTree.getBlockId()+", "+merkleTree.getTxCount()+", CAST('"+merkleTree.getMerkleRoot()+"' AS bytea), ARRAY['"+nodeMapKey[0]+", "+nodeMapKey[1]+"'], ARRAY[CAST('"+nodeMapValue[0]+"' AS bytea), CAST('"+nodeMapValue[1]+"' AS bytea)])";
            result = statement.execute(saveStatement);
//            statement.setLong(1, merkleTree.getBlockId());
//            statement.setLong(2, merkleTree.getTxCount());
//            statement.setBytes(3, merkleTree.getMerkleRoot());
//            statement.setArray(4, array);
//            result = statement.executeUpdate();
        } catch(SQLException me) {
            log.error(me.getMessage());
            me.printStackTrace();
        }
        return result;

    }

    public int insert(MerkleTree merkleTree) {

        log.debug("insert: "+merkleTree.toString());

        AtomicInteger i = new AtomicInteger(0);
        String[] nodeMapKey = new String[merkleTree.getNodeMap().size()];
        byte[][] nodeMapValue = new byte[merkleTree.getNodeMap().size()][];
        merkleTree.getNodeMap().forEach((key, value) -> {
            nodeMapKey[i.get()] = key.toString();
            nodeMapValue[i.get()] = value;
            log.debug("insert - nodeMapKey: "+nodeMapKey[i.get()]+", nodeMapValue: "+Arrays.toString(nodeMapValue[i.get()]));
            i.getAndIncrement();
        });

        int result = 0;
        try {
            Array keyArray = connection.createArrayOf("text", nodeMapKey);
            Array valueArray = connection.createArrayOf("bytea", nodeMapValue);
            PreparedStatement statement = connection.prepareStatement(saveStatement);
            statement.setLong(1, merkleTree.getBlockId());
            statement.setLong(2, merkleTree.getTxCount());
            statement.setBytes(3, merkleTree.getMerkleRoot());
            statement.setArray(4, keyArray);
            statement.setArray(5, valueArray);
            result = statement.executeUpdate();
        } catch(SQLException ex) {
            ex.printStackTrace();
        }
        return result;
    }
}
