package com.example.financialtrackerjavafx.Transaksi;

import com.example.financialtrackerjavafx.Laporan.LaporanKeuangan;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;

public class Pengeluaran extends Transaksi {

    public Pengeluaran(int inputJumlah, String kategori) {
        super(inputJumlah, kategori);
    }

    @Override
    public void inputTransaksi() {
        Stage stage = new Stage();
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Label amountLabel = new Label("Jumlah Pengeluaran:");
        TextField amountField = new TextField();
        amountField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                amountField.setText(oldValue);
            }
        });

        Label categoryLabel = new Label("Kategori:");
        TextField categoryField = new TextField();

        Button submitBtn = new Button("Submit");
        Label statusLabel = new Label();

        grid.add(amountLabel, 0, 0);
        grid.add(amountField, 1, 0);
        grid.add(categoryLabel, 0, 1);
        grid.add(categoryField, 1, 1);
        grid.add(submitBtn, 1, 2);
        grid.add(statusLabel, 1, 3);

        submitBtn.setOnAction(e -> {
            try {
                int amount = Integer.parseInt(amountField.getText());
                String category = categoryField.getText();

                if (amount <= 0 || category.isEmpty()) {
                    statusLabel.setText("Mohon isi semua field dengan benar!");
                    statusLabel.setStyle("-fx-text-fill: red;");
                    return;
                }

                this.setInputJumlah(amount);
                this.setKategori(category);

                LaporanKeuangan laporan = new LaporanKeuangan();
                laporan.writeTransaksi(amount, category, false);
                rincianTransaksi();

                statusLabel.setText("Pengeluaran berhasil dicatat!");
                statusLabel.setStyle("-fx-text-fill: green;");

            } catch (NumberFormatException ex) {
                statusLabel.setText("Jumlah harus berupa angka!");
                statusLabel.setStyle("-fx-text-fill: red;");
            } catch (IOException ex) {
                statusLabel.setText("Gagal menyimpan data!");
                statusLabel.setStyle("-fx-text-fill: red;");
            }
        });

        Scene scene = new Scene(grid, 350, 250);
        stage.setScene(scene);
        stage.setTitle("Input Pengeluaran");
        stage.show();
    }

    @Override
    public void rincianTransaksi() {
        System.out.println("\n=== Rincian Pengeluaran ===");
        System.out.println("Jumlah: Rp" + getInputJumlah());
        System.out.println("Kategori: " + getKategori());
        System.out.println("Tanggal: " + getToday());
        System.out.println("==========================\n");
    }
}