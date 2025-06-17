// SavingsTargetService.java
package com.example.fttry;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SavingsTargetService {
    private final DatabaseHelper dbHelper = DatabaseHelper.getInstance();

    public void addSavingsTarget(SavingsTarget target) throws SQLException {
        String sql = "INSERT INTO savings_targets(name, target_amount, current_amount, target_date, created_at) " +
                "VALUES(?, ?, ?, ?, ?)";
        dbHelper.executeTransaction(sql,
                target.getName(),
                target.getTargetAmount(),
                target.getCurrentAmount(),
                target.getTargetDate().toString(),
                target.getCreatedAt().toString());
    }

    public void updateSavingsTarget(SavingsTarget target) throws SQLException {
        String sql = "UPDATE savings_targets SET name=?, target_amount=?, current_amount=?, target_date=? WHERE id=?";
        dbHelper.executeTransaction(sql,
                target.getName(),
                target.getTargetAmount(),
                target.getCurrentAmount(),
                target.getTargetDate().toString(),
                target.getId());
    }

    public void deleteSavingsTarget(int id) throws SQLException {
        String sql = "DELETE FROM savings_targets WHERE id=?";
        dbHelper.executeTransaction(sql, id);
    }

    public List<SavingsTarget> getAllSavingsTargets() throws SQLException {
        List<SavingsTarget> targets = new ArrayList<>();
        String sql = "SELECT * FROM savings_targets ORDER BY target_date";

        try (Connection conn = dbHelper.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                SavingsTarget target = new SavingsTarget(
                        rs.getString("name"),
                        rs.getDouble("target_amount"),
                        LocalDate.parse(rs.getString("target_date"))
                );
                target.setId(rs.getInt("id"));
                target.setCurrentAmount(rs.getDouble("current_amount"));
                target.setCreatedAt(LocalDate.parse(rs.getString("created_at")));
                targets.add(target);
            }
        }
        return targets;
    }

    public void addToSavings(int targetId, double amount) throws SQLException {
        String sql = "UPDATE savings_targets SET current_amount = current_amount + ? WHERE id = ?";
        dbHelper.executeTransaction(sql, amount, targetId);
    }
}