package com.ll.vbc.h2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class H2Config {

    public static Connection getConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection("jdbc:h2:./consensusLogA", "vbc", "vbcpw");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return connection;
    }


}
