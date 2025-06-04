package com.example.financialtrackerjavafx.Laporan;

import com.example.financialtrackerjavafx.Transaksi.Transaksi;

public class TransactionData extends Transaksi {
    private final String type;
    private final String amount;
    private final String category;
    private final String date;
    private final String time;

    public TransactionData(String type, String amount, String category, String date, String time) {
        super();
        this.type = type;
        this.amount = amount;
        this.category = category;
        this.date = date;
        this.time = time;
    }

    @Override
    public void inputTransaksi() {

    }

    @Override
    public void rincianTransaksi() {

    }

    // Getters
    public String getType() {
        return type;
    }

    public String getAmount() {
        return amount;
    }

    public String getCategory() {
        return category;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }
}