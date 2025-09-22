package com.github.ojvzinn.desafiopicpay.repository;

import com.github.ojvzinn.desafiopicpay.database.Database;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRepository {

    public void createUser(String name, String register, String email, String password, BigDecimal balance, int userTypeID) {
        try (Connection conn = Database.getDataSource().getConnection()) {
            String SQL = "INSERT INTO users(`name`, `register`, `email`, `password`, `balance`, `userType`) VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(SQL)) {
                stmt.setString(1, name);
                stmt.setString(2, register);
                stmt.setString(3, email);
                stmt.setString(4, password);
                stmt.setBigDecimal(5, balance.setScale(2, BigDecimal.ROUND_HALF_UP));
                stmt.setInt(6, userTypeID);
                stmt.executeUpdate();
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void updateUser(long id, String name, String register, String email, String password, BigDecimal balance, int userTypeID) {
        try (Connection conn = Database.getDataSource().getConnection()) {
            String SQL = "UPDATE users SET `name` = ?, `register` = ?, `email` = ?, `password` = ?, `balance` = ?, `userType` = ? WHERE `id` = ?";
            try (PreparedStatement stmt = conn.prepareStatement(SQL)) {
                stmt.setString(1, name);
                stmt.setString(2, register);
                stmt.setString(3, email);
                stmt.setString(4, password);
                stmt.setBigDecimal(5, balance.setScale(2, BigDecimal.ROUND_HALF_UP));
                stmt.setInt(6, userTypeID);
                stmt.setLong(7, id);
                stmt.executeUpdate();
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    public JSONObject findUserByID(long id) {
        JSONObject json = new JSONObject();
        try (Connection conn = Database.getDataSource().getConnection()) {
            String SQL = "SELECT * FROM users WHERE `id` = ?";
            try (PreparedStatement stmt = conn.prepareStatement(SQL)) {
                stmt.setLong(1, id);
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        json.put("id", id);
                        json.put("name", rs.getString("name"));
                        json.put("register", rs.getString("register"));
                        json.put("email", rs.getString("email"));
                        json.put("password", rs.getString("password"));
                        json.put("balance", rs.getBigDecimal("balance").setScale(2, BigDecimal.ROUND_HALF_UP));
                        json.put("userTypeID", rs.getInt("userType"));
                    }
                }
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }

        return json;
    }

    public boolean containsUserWithEmailAndRegister(String email, String register) {
        try (Connection conn = Database.getDataSource().getConnection()) {
            String SQL = "SELECT * FROM users WHERE `email` = ? OR `register` = ?";
            try (PreparedStatement stmt = conn.prepareStatement(SQL)) {
                stmt.setString(1, email);
                stmt.setString(2, register);
                try (ResultSet rs = stmt.executeQuery()) {
                    return rs.next();
                }
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }
}
