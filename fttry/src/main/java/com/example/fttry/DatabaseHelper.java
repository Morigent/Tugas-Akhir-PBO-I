package com.example.fttry;

import java.sql.*;

public class DatabaseHelper {
    private static final String DB_URL = "jdbc:sqlite:financial_tracker.db";
    private static DatabaseHelper instance;
    private Connection connection;

    private DatabaseHelper() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(DB_URL);
            createTables();
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException("Failed to initialize database", e);
        }
    }

    public static synchronized DatabaseHelper getInstance() {
        if (instance == null) {
            instance = new DatabaseHelper();
        }
        return instance;
    }

    private void createTables() {
        String createTransaksiTable = "CREATE TABLE IF NOT EXISTS transaksi (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "jenis TEXT NOT NULL CHECK (jenis IN ('Pemasukkan', 'Pengeluaran'))," +
                "jumlah REAL NOT NULL CHECK (jumlah > 0)," +
                "kategori TEXT NOT NULL," +
                "deskripsi TEXT," +
                "tanggal TEXT NOT NULL)";

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createTransaksiTable);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to create tables", e);
        }
    }

    public Connection getConnection() {
        return connection;
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

    public void executeTransaction(String sql, Object... params) throws SQLException {
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                pstmt.setObject(i + 1, params[i]);
            }
            pstmt.executeUpdate();
        }
    }
}