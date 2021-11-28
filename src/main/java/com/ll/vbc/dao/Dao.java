package com.ll.vbc.dao;

import com.ll.vbc.h2.H2Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Array;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class Dao {
    private static final Logger log = LoggerFactory.getLogger(Dao.class);
    public Connection connection;
    public Dao() {
        try {
            connection = H2Config.getConnection();
            log.debug(connection.getSchema());
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    public Connection getConnection() {
        return connection;
    }

    protected static class MapArray {

        private final Array keyArray;
        private final Array valueArray;
        private MapArray(Array keyArray, Array valueArray) {
            this.keyArray = keyArray;
            this.valueArray = valueArray;
        }
        public Array getKeyArray() {
            return keyArray;
        }
        public Array getValueArray() {
            return valueArray;
        }
    }

    protected MapArray convertMapToSqlArray(Map<UUID, byte[]> theMap) throws SQLException {

        AtomicInteger i = new AtomicInteger(0);
        String[] nodeMapKey = new String[theMap.size()];
        byte[][] nodeMapValue = new byte[theMap.size()][];
        theMap.forEach((key, value) -> {
            nodeMapKey[i.get()] = key.toString();
            nodeMapValue[i.get()] = value;
            log.debug("insert - nodeMapKey: "+nodeMapKey[i.get()]+", nodeMapValue: "+ Arrays.toString(nodeMapValue[i.get()]));
            i.getAndIncrement();
        });
        Array keyArray = connection.createArrayOf("text", nodeMapKey);
        Array valueArray = connection.createArrayOf("bytea", nodeMapValue);
        return new MapArray(keyArray, valueArray);
    }
}
