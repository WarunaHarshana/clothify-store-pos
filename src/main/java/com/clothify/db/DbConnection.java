package com.clothify.db;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class DbConnection {

    private static DbConnection instance;
    private Connection connection;

    private DbConnection() {
    }

    public static DbConnection getInstance() {
        return instance == null ? (instance = new DbConnection()) : instance;
    }

    public Connection getConnection() throws Exception {
        if (connection == null || connection.isClosed()) {
            Properties props = new Properties();
            try (InputStream input = getClass().getClassLoader().getResourceAsStream("db.properties")) {
                if (input == null) throw new RuntimeException("db.properties not found");
                props.load(input);
            }
            Class.forName(props.getProperty("db.driver"));
            connection = DriverManager.getConnection(
                    props.getProperty("db.url"),
                    props.getProperty("db.username"),
                    props.getProperty("db.password")
            );
        }
        return connection;
    }
}

