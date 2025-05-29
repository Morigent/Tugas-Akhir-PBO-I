package com.example.financialtrackerjavafx.Transaksi;

import com.example.financialtrackerjavafx.Laporan.LaporanKeuangan;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Pemasukan extends Transaksi {
    private int inputJumlah;
    private String kategori;

    public Pemasukan(int inputJumlah, String kategori) {
        super(inputJumlah, kategori);
    }

    @Override
    public LocalDate getToday() {
        return super.getToday();
    }

    @Override
    public void inputTransaksi() {
        VBox root = new VBox(10);
        root.setAlignment(Pos.CENTER);
        Separator separator = new Separator();
        HBox hbox = new HBox(10);
        root.setAlignment(Pos.CENTER);

        TextField inJumlah = new TextField();
        inJumlah.setPromptText("Jumlah Pemasukkan");

        inJumlah.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                inJumlah.setText(oldValue);
            }
        });

        TextField inKategori =new TextField();
        inKategori.setPromptText("Kategori");

        Button submit =  new Button("Submit");

        root.getChildren().addAll(inJumlah, inKategori,submit,hbox);



        Scene scene = new Scene(root, 300, 200);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();

        submit.setOnAction(event -> {
            inputJumlah = Integer.parseInt(inJumlah.getText());
            kategori = inKategori.getText();

            LaporanKeuangan pemasukkan = new LaporanKeuangan();
            pemasukkan.writePemasukkan(inputJumlah, kategori);
        });
    }

    @Override
    public void rincianTransaksi() {
        LocalTime sekarang = LocalTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        String waktuFormatted = sekarang.format(formatter);
        System.out.println("Waktu sekarang: " + waktuFormatted);
        System.out.println("\n=====================================");
        System.out.println("Jumlah Transaksi.Transaksi: Rp." + inputJumlah);
        System.out.println("Kategori        : "  + kategori);
        System.out.println("Tanggal & Waktu : " + getToday() + ", " + waktuFormatted);
        System.out.println("=====================================\n");
    }
}