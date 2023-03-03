package com.example.service.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;

import com.miragesql.miragesql.provider.ConnectionProvider;

public class ConnectionProviderImpl implements ConnectionProvider {
    private Logger logger = Logger.getLogger(ConnectionProviderImpl.class.getName());
    private DatabaseConfiguration databaseConfiguration = new DatabaseConfiguration();

    private String postgreUrl = "jdbc:postgresql://localhost:5432/postgres";

    private String postgreUsername = "postgres";

    private String postgrePassword = "admin";

    private String sqlServerUrl = "jdbc:sqlserver://localhost:1433;DatabaseName=BookDatabase";

    private String sqlServerUsername = "sqlserver";

    private String sqlServerPassword = "admin";

    @Override
    public Connection getConnection() {
        Connection conn = null;
        try {
            // logger.info(">>> getConnection(): connected to PostgreSQL");
            // conn = DriverManager.getConnection(postgreUrl, postgreUsername, postgrePassword);
            logger.info(">>> getConnection(): connected to SqlServer");
            conn = DriverManager.getConnection(sqlServerUrl, sqlServerUsername, sqlServerPassword);
        } catch (SQLException e) {
            logger.severe(">>> getConnection() error:" + e.getMessage());
        }
        return conn;
    }
}
