package com.github.ojvzinn.desafiopicpay.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {

    @Getter
    private static HikariDataSource dataSource;

    public static void init() {
        HikariConfig config = new HikariConfig();
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("useServerPrepStmts", "true");
        config.addDataSourceProperty("useLocalSessionState", "true");
        config.addDataSourceProperty("cacheResultSetMetadata", "true");
        config.addDataSourceProperty("cacheServerConfiguration", "true");
        config.addDataSourceProperty("elideSetAutoCommits", "true");
        config.addDataSourceProperty("autoReconnect", "true");
        config.setAutoCommit(true);
        config.setMinimumIdle(10);
        config.setMaximumPoolSize(100);
        config.setConnectionTimeout(5000);
        config.setIdleTimeout(5000);
        config.setValidationTimeout(3000);
        config.setMaxLifetime(27000);
        config.setUsername("root");
        config.setPassword("");
        config.setDriverClassName("com.mysql.jdbc.Driver");
        config.setJdbcUrl("jdbc:mysql://localhost:3306/server");
        dataSource = new HikariDataSource(config);
        initTable();
    }

    private static void initTable() {
        String userSQL = "CREATE TABLE IF NOT EXISTS USERS(`ID` BIGINT PRIMARY KEY AUTO_INCREMENT, `name` TEXT NOT NULL, `register` TEXT NOT NULL UNIQUE, `email` TEXT NOT NULL UNIQUE, `password` TEXT NOT NULL, `balance` DECIMAL(19, 2), `userType` INT UNIQUE);";
        String transactionSQL = "CREATE TABLE IF NOT EXISTS TRANSACTION(`ID` BIGINT PRIMARY KEY AUTO_INCREMENT, `payerID` BIGINT, `payeeID` BIGINT, `amount` DECIMAL(19, 2))";
        try (Connection conn = dataSource.getConnection()) {
            try (Statement statement = conn.createStatement()) {
                statement.execute(userSQL);
                statement.execute(transactionSQL);
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

}
