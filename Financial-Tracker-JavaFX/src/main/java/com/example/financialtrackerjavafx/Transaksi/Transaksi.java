package com.example.financialtrackerjavafx.Transaksi;

import java.time.LocalDate;

public abstract class Transaksi {
    private int inputJumlah;
    private String kategori;
    private  LocalDate today = LocalDate.now();

    // Constructor
    public Transaksi (int inputJumlah, String kategori){
        this.inputJumlah = inputJumlah;
        this.kategori = kategori;
    }

    public Transaksi() {


    }

    // Getter
    public int getInputJumlah() {
        return inputJumlah;
    }

    public String getKategori() {
        return kategori;
    }

    public LocalDate getToday() {
        return today;
    }

    // Setter
    public void setInputJumlah(int inputJumlah) {
        this.inputJumlah = inputJumlah;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }


    // method input transaksi
    public abstract void inputTransaksi ();

    public abstract void rincianTransaksi ();

}
