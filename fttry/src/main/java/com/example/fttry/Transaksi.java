package com.example.fttry;

import java.time.LocalDate;

public class Transaksi {
    private int id;
    private String jenis;
    private double jumlah;
    private String kategori;
    private String deskripsi;
    private LocalDate tanggal;

    public Transaksi() {}

    public Transaksi(String jenis, double jumlah, String kategori, String deskripsi, LocalDate tanggal) {
        this.jenis = jenis;
        this.jumlah = jumlah;
        this.kategori = kategori;
        this.deskripsi = deskripsi;
        this.tanggal = tanggal;
    }

    // Getters and setters
    public int getId() { return id; }
    public String getJenis() { return jenis; }
    public double getJumlah() { return jumlah; }
    public String getKategori() { return kategori; }
    public String getDeskripsi() { return deskripsi; }
    public LocalDate getTanggal() { return tanggal; }

    public void setId(int id) { this.id = id; }
    public void setJenis(String jenis) { this.jenis = jenis; }
    public void setJumlah(double jumlah) { this.jumlah = jumlah; }
    public void setKategori(String kategori) { this.kategori = kategori; }
    public void setDeskripsi(String deskripsi) { this.deskripsi = deskripsi; }
    public void setTanggal(LocalDate tanggal) { this.tanggal = tanggal; }

    @Override
    public String toString() {
        return "Transaksi{" +
                "id=" + id +
                ", jenis='" + jenis + '\'' +
                ", jumlah=" + jumlah +
                ", kategori='" + kategori + '\'' +
                ", deskripsi='" + deskripsi + '\'' +
                ", tanggal=" + tanggal +
                '}';
    }
}