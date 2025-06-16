package com.example.fttry;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class TransactionService {
    private final DatabaseHelper dbHelper =DatabaseHelper.getInstance();


    public void addTransaction(Transaksi transaksi) throws SQLException {
        String sql = "INSERT INTO transaksi(jenis, jumlah, kategori, deskripsi, tanggal) VALUES(?,?,?,?,?)";
        dbHelper.executeTransaction(sql,
                transaksi.getJenis(),
                transaksi.getJumlah(),
                transaksi.getKategori(),
                transaksi.getDeskripsi(),
                transaksi.getTanggal().toString());
    }

    public void updateTransaction(Transaksi transaksi) throws SQLException {
        String sql = "UPDATE transaksi SET jenis=?, jumlah=?, kategori=?, deskripsi=?, tanggal=? WHERE id=?";
        dbHelper.executeTransaction(sql,
                transaksi.getJenis(),
                transaksi.getJumlah(),
                transaksi.getKategori(),
                transaksi.getDeskripsi(),
                transaksi.getTanggal().toString(),
                transaksi.getId());
    }

    public void deleteTransaction(int id) throws SQLException {
        String sql = "DELETE FROM transaksi WHERE id=?";
        dbHelper.executeTransaction(sql, id);
    }

    public List<Transaksi> getAllTransactions() throws SQLException {
        List<Transaksi> transactions = new ArrayList<>();
        String sql = "SELECT * FROM transaksi ORDER BY tanggal DESC, id DESC";

        try (Connection conn = dbHelper.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Transaksi transaksi = new Transaksi(
                        rs.getString("jenis"),
                        rs.getDouble("jumlah"),
                        rs.getString("kategori"),
                        rs.getString("deskripsi"),
                        LocalDate.parse(rs.getString("tanggal"))
                );
                transaksi.setId(rs.getInt("id"));
                transactions.add(transaksi);
            }
        }
        return transactions;
    }

    public double calculateBalance() throws SQLException {
        double balance = 0;
        String sql = "SELECT jenis, SUM(jumlah) as total FROM transaksi GROUP BY jenis";

        try (Connection conn = dbHelper.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String jenis = rs.getString("jenis");
                double total = rs.getDouble("total");
                balance += "Pemasukkan".equals(jenis) ? total : -total;
            }
        }
        return balance;
    }

    public List<String> getCategories() throws SQLException {
        List<String> categories = new ArrayList<>();
        String sql = "SELECT DISTINCT kategori FROM transaksi ORDER BY kategori";

        try (Connection conn = dbHelper.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                categories.add(rs.getString("kategori"));
            }
        }
        return categories;
    }

    public Map<String, Integer> getTransactionSummary() {
        return null;
    }

    public Map<String, Integer> getTransactionSummaryByCategory() {
        return Map.of();
    }

    public Map<LocalDate, Double> getMonthlyIncome() throws SQLException {
        Map<LocalDate, Double> incomeMap = new TreeMap<>();
        String sql = "SELECT date(tanggal, 'start of month') as month, SUM(jumlah) as total " +
                "FROM transaksi WHERE jenis = 'Pemasukkan' " +
                "GROUP BY strftime('%Y-%m', tanggal) " +
                "ORDER BY month";

        try (Connection conn = dbHelper.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                incomeMap.put(
                        LocalDate.parse(rs.getString("month")),
                        rs.getDouble("total")
                );
            }
        }
        return incomeMap;
    }

    public Map<LocalDate, Double> getMonthlyExpenses() throws SQLException {
        Map<LocalDate, Double> expenseMap = new TreeMap<>();
        String sql = "SELECT date(tanggal, 'start of month') as month, SUM(jumlah) as total " +
                "FROM transaksi WHERE jenis = 'Pengeluaran' " +
                "GROUP BY strftime('%Y-%m', tanggal) " +
                "ORDER BY month";

        try (Connection conn = dbHelper.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                expenseMap.put(
                        LocalDate.parse(rs.getString("month")),
                        rs.getDouble("total")
                );
            }
        }
        return expenseMap;
    }
}