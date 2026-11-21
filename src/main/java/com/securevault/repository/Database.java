package com.securevault.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public final class Database {
    private static final String URL = "jdbc:sqlite:securevault.db";

    private Database() {
    }

    public static Connection openConnection() throws SQLException {
        Properties options = new Properties();
        options.setProperty("foreign_keys", "true");
        return DriverManager.getConnection(URL, options);
    }
}