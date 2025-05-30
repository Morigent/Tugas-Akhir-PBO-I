package com.example.financialtrackerjavafx.Laporan;

import java.io.*;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class LaporanKeuangan {
    private final String filePemasukan = "Pemasukan.CSV";
    private final String filePengeluaran = "Pengeluaran.CSV";
    private final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));

    public LaporanKeuangan() {
        try {
            new File(filePemasukan).createNewFile();
            new File(filePengeluaran).createNewFile();
        } catch (IOException e) {
            System.err.println("Gagal membuat file: " + e.getMessage());
        }
    }

    public void writeTransaksi(int jumlah, String kategori, boolean isPemasukan) throws IOException {
        String filename = isPemasukan ? filePemasukan : filePengeluaran;
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename, true))) {
            String waktu = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));
            LocalDate tanggal = LocalDate.now();
            String formattedAmount = "Rp" + NumberFormat.getNumberInstance(Locale.US).format(jumlah) + ",00";

            // Match the exact format in your Pemasukan.CSV
            String data = String.format("%s,%s,%s,%s\n",
                    formattedAmount, kategori, tanggal, waktu);
            bw.write(data);
        }
    }

    public String bacaLaporan(boolean isPemasukan) throws IOException {
        String filename = isPemasukan ? filePemasukan : filePengeluaran;
        StringBuilder content = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                content.append(line).append("\n");
            }
        }
        return content.toString();
    }

    public List<String[]> getTransaksiData(boolean isPemasukan) throws IOException {
        String filename = isPemasukan ? filePemasukan : filePengeluaran;
        List<String[]> data = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    // Split on comma but ignore commas inside quoted values
                    String[] parts = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
                    data.add(parts);
                }
            }
        }
        return data;
    }

    public Map<String, double[]> getMonthlySummary() throws IOException {
        Map<String, double[]> summary = new TreeMap<>(); // month-year as key

        // Process income
        List<String[]> pemasukan = getTransaksiData(true);
        for (String[] trans : pemasukan) {
            String monthYear = LocalDate.parse(trans[2].trim()).format(DateTimeFormatter.ofPattern("MMM-yyyy"));
            double amount = Double.parseDouble(trans[0].replaceAll("[^\\d]", ""));
            summary.computeIfAbsent(monthYear, k -> new double[2])[0] += amount;
        }

        // Process expenses
        List<String[]> pengeluaran = getTransaksiData(false);
        for (String[] trans : pengeluaran) {
            String monthYear = LocalDate.parse(trans[2].trim()).format(DateTimeFormatter.ofPattern("MMM-yyyy"));
            double amount = Double.parseDouble(trans[0].replaceAll("[^\\d]", ""));
            summary.computeIfAbsent(monthYear, k -> new double[2])[1] += amount;
        }

        return summary;
    }
}