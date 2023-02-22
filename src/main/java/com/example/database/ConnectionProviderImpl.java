package com.example.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.miragesql.miragesql.provider.ConnectionProvider;

public class ConnectionProviderImpl implements ConnectionProvider {
    private DatabaseConfiguration databaseConfiguration = new DatabaseConfiguration();

    private String url = "jdbc:postgresql://localhost:5432/postgres";

    private String username = "postgres";

    private String password = "admin";

    @Override
    public Connection getConnection() {
        Connection conn = null;
        try {
            System.out.println(databaseConfiguration.getUrl());
            // System.out.println(url);
            // System.out.println(user);
            // System.out.println(password);
            conn = DriverManager.getConnection(url, username, password);
            System.out.println(">>> connected to PostgreSQL");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }
}
