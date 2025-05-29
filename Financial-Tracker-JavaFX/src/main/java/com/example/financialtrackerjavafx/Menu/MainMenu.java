package com.example.financialtrackerjavafx.Menu;

import com.example.financialtrackerjavafx.Transaksi.Pemasukan;
import com.example.financialtrackerjavafx.Transaksi.Pengeluaran;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import com.example.financialtrackerjavafx.Laporan.LaporanGUI;

public class MainMenu extends Application {
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Financial Tracker");

        Label titleLabel = new Label("Financial Tracker");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        Button incomeBtn = new Button("Catat Pemasukan");
        incomeBtn.setStyle("-fx-font-size: 14px; -fx-min-width: 200px;");

        Button expenseBtn = new Button("Catat Pengeluaran");
        expenseBtn.setStyle("-fx-font-size: 14px; -fx-min-width: 200px;");

        Button reportBtn = new Button("Lihat Laporan");
        reportBtn.setStyle("-fx-font-size: 14px; -fx-min-width: 200px;");

        VBox layout = new VBox(20);
        layout.setPadding(new Insets(30, 30, 30, 30));
        layout.setAlignment(Pos.CENTER);
        layout.getChildren().addAll(titleLabel, incomeBtn, expenseBtn, reportBtn);

        incomeBtn.setOnAction(e -> {
            Pemasukan pemasukan = new Pemasukan(0, "");
            pemasukan.inputTransaksi();
        });

        expenseBtn.setOnAction(e -> {
            Pengeluaran pengeluaran = new Pengeluaran(0, "");
            pengeluaran.inputTransaksi();
        });

        reportBtn.setOnAction(e -> {
            LaporanGUI laporanGUI = new LaporanGUI();
            laporanGUI.showLaporan();
        });

        Scene scene = new Scene(layout, 400, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}