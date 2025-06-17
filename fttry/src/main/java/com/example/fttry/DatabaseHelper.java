package com.example.fttry;

import java.sql.*;

public class DatabaseHelper {
    private static final String DB_URL = "jdbc:sqlite:financial_tracker.db";
    private static DatabaseHelper instance;
    private Connection connection;

    private DatabaseHelper() {
        try {
            connection = DriverManager.getConnection(DB_URL);
            // Pastikan auto-commit sesuai kebutuhan
            connection.setAutoCommit(true); // Default untuk SQLite
            createTables();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to initialize database", e);
        }
    }

    public static synchronized DatabaseHelper getInstance() {
        if (instance == null) {
            instance = new DatabaseHelper();
        }
        return instance;
    }

    private void createTables() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS transaksi (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "jenis TEXT NOT NULL CHECK (jenis IN ('Pemasukkan', 'Pengeluaran'))," +
                "jumlah REAL NOT NULL CHECK (jumlah > 0)," +
                "kategori TEXT NOT NULL," +
                "deskripsi TEXT," +
                "tanggal TEXT NOT NULL)";

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
        }
        String savingsTable = "CREATE TABLE IF NOT EXISTS savings_targets (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT NOT NULL," +
                "target_amount REAL NOT NULL CHECK (target_amount > 0)," +
                "current_amount REAL NOT NULL DEFAULT 0," +
                "target_date TEXT NOT NULL," +
                "created_at TEXT NOT NULL)";

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(savingsTable);
        }
    }

    public Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(DB_URL);
            connection.setAutoCommit(true); // Pastikan konsisten
        }
        return connection;
    }

    public void executeTransaction(String sql, Object... params) throws SQLException {
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            for (int i = 0; i < params.length; i++) {
                pstmt.setObject(i + 1, params[i]);
            }
            pstmt.executeUpdate();
        }
    }

    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}