package com.github.ojvzinn.desafiopicpay.repository;

import com.github.ojvzinn.desafiopicpay.database.Database;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TransactionRepository {

    public void createTransaction(Long payerID, Long payeeID, BigDecimal amount) {
        try (Connection conn = Database.getDataSource().getConnection()) {
            String SQL = "INSERT INTO transaction(`payerID`, `payeeID`, `amount`) VALUES (?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(SQL)) {
                stmt.setLong(1, payerID);
                stmt.setLong(2, payeeID);
                stmt.setBigDecimal(3, amount.setScale(2, BigDecimal.ROUND_HALF_UP));
                stmt.executeUpdate();
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    public JSONObject findTransactionByID(long id) {
        JSONObject json = new JSONObject();
        try (Connection conn = Database.getDataSource().getConnection()) {
            String SQL = "SELECT * FROM transaction WHERE `id` = ?";
            try (PreparedStatement stmt = conn.prepareStatement(SQL)) {
                stmt.setLong(1, id);
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        json.put("id", id);
                        json.put("payerID", rs.getLong("payerID"));
                        json.put("payeeID", rs.getLong("payeeID"));
                        json.put("amount", rs.getBigDecimal("amount").setScale(2, BigDecimal.ROUND_HALF_UP));
                    }
                }
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }

        return json;
    }

}
