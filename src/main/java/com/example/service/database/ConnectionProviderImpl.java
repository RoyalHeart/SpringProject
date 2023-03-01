package com.example.service.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;

import com.miragesql.miragesql.provider.ConnectionProvider;

public class ConnectionProviderImpl implements ConnectionProvider {
    private Logger logger = Logger.getLogger(ConnectionProviderImpl.class.getName());
    private DatabaseConfiguration databaseConfiguration = new DatabaseConfiguration();

    private String url = "jdbc:postgresql://localhost:5432/postgres";

    private String username = "postgres";

    private String password = "admin";

    @Override
    public Connection getConnection() {
        Connection conn = null;
        try {
            logger.info(">>> getConnection(): connected to PostgreSQL");
            conn = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            logger.severe(">>> getConnection() error:" + e.getMessage());
        }
        return conn;
    }
}
