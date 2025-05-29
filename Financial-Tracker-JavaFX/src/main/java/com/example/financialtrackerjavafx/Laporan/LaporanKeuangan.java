package com.example.financialtrackerjavafx.Laporan;

import java.io.*;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class LaporanKeuangan {
    private final String filePemasukan = "Pemasukan.CSV";
    private final String filePengeluaran = "Pengeluaran.CSV";

    public void writePemasukkan(int inputJumlah, String kategori) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePemasukan, true))) {
            String waktu = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));
            LocalDate tanggal = LocalDate.now();
            String formatted = NumberFormat.getCurrencyInstance().format(inputJumlah);

            String data = String.format("Rp.%d, %s, %s %s\n",  formatted, kategori, tanggal, waktu);
            bw.write(data);


        }
        catch (IOException e) {
            System.out.println("Gagal menyimpan data: " + e.getMessage());
        }
    }

    public void writePengeluaran(int inputJumlah, String kategori) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePengeluaran, true))) {
            String waktu = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));
            LocalDate tanggal = LocalDate.now();
            String formatted = NumberFormat.getCurrencyInstance().format(inputJumlah);

            String data = String.format("Rp.%d, %s, %s %s\n",  formatted, kategori, tanggal, waktu);
            bw.write(data);


        }
        catch (IOException e) {
            System.out.println("Gagal menyimpan data: " + e.getMessage());
        }
    }

}
