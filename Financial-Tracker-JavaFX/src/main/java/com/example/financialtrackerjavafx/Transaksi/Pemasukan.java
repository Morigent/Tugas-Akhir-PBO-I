package com.example.financialtrackerjavafx.Transaksi;

import com.example.financialtrackerjavafx.Laporan.LaporanKeuangan;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class Pemasukan extends Transaksi {

    public Pemasukan(int inputJumlah, String kategori) {
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

        Label amountLabel = new Label("Jumlah Pemasukan:");
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
                laporan.writeTransaksi(amount, category, true);

                // Show transaction details in new window
                showTransactionDetails(amount, category, true);

                stage.close(); // Close the input window
            } catch (Exception ex) {
                statusLabel.setText("Error: " + ex.getMessage());
                statusLabel.setStyle("-fx-text-fill: red;");
            }
        });
        ;

        Scene scene = new Scene(grid, 350, 250);
        stage.setScene(scene);
        stage.setTitle("Input Pemasukan");
        stage.show();
    }

    private void showTransactionDetails(int amount, String category, boolean isIncome) {
        Stage detailsStage = new Stage();
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);

        Label title = new Label(isIncome ? "Rincian Pemasukan" : "Rincian Pengeluaran");
        title.setStyle("-fx-font-size: 18; -fx-font-weight: bold;");

        Label amountLabel = new Label("Jumlah: " + NumberFormat.getCurrencyInstance(new Locale("id", "ID")).format(amount));
        Label categoryLabel = new Label("Kategori: " + category);
        Label dateLabel = new Label("Tanggal: " + LocalDate.now());
        Label timeLabel = new Label("Waktu: " + LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm")));

        Button closeBtn = new Button("Tutup");
        closeBtn.setOnAction(e -> detailsStage.close());

        layout.getChildren().addAll(title, new Separator(), amountLabel, categoryLabel, dateLabel, timeLabel, closeBtn);

        Scene scene = new Scene(layout, 300, 250);
        detailsStage.setScene(scene);
        detailsStage.setTitle("Rincian Transaksi");
        detailsStage.show();
    }

    @Override
    public void rincianTransaksi() {

    }
}

