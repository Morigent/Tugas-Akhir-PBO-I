package com.example.financialtrackerjavafx.Laporan;

import com.example.financialtrackerjavafx.Transaksi.Transaksi;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class LaporanGUI {
    private final LaporanKeuangan laporan;
    private final NumberFormat currencyFormat;

    public LaporanGUI() {
        this.laporan = new LaporanKeuangan();
        this.currencyFormat = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
    }

    public void showLaporan() {
        Stage stage = new Stage();
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(15));

        try {
            // Tab pane for table and chart
            TabPane tabPane = new TabPane();

            // Transaction Table Tab
            Tab tableTab = new Tab("Daftar Transaksi");
            tableTab.setContent(createTransactionTable());
            tableTab.setClosable(false);

            // Chart Tab
            Tab chartTab = new Tab("Grafik Bulanan");
            chartTab.setContent(createMonthlyChart());
            chartTab.setClosable(false);

            tabPane.getTabs().addAll(tableTab, chartTab);
            root.setCenter(tabPane);

            // Add close button at bottom
            Button closeBtn = new Button("Tutup");
            closeBtn.setOnAction(e -> stage.close());
            root.setBottom(closeBtn);

        } catch (IOException e) {
            root.setCenter(new Label("Gagal memuat laporan: " + e.getMessage()));
        }

        Scene scene = new Scene(root, 800, 600);
        stage.setTitle("Laporan Keuangan");
        stage.setScene(scene);
        stage.show();
    }

    private TableView<Transaksi> createTransactionTable() throws IOException {
        TableView<Transaksi> table = new TableView<>();
        ObservableList<Transaksi> data = FXCollections.observableArrayList();

        // Add income data
        List<String[]> pemasukan = laporan.getTransaksiData(true);
        for (String[] trans : pemasukan) {
            try {
                // Parse the amount properly from "Rp10.000,00" format
                String amountStr = trans[0].replace("Rp", "").replace(".", "").replace(",00", "");
                String formattedAmount = "Rp" + NumberFormat.getNumberInstance(Locale.US).format(Integer.parseInt(amountStr)) + ",00";

                data.add(new Transaksi(
                        "Pemasukan",
                        formattedAmount,
                        trans[1], // category
                        trans[2], // date
                        trans[3]  // time
                ) {
                });
            } catch (Exception e) {
                System.err.println("Error parsing transaction: " + Arrays.toString(trans));
            }

        // Add expense data
        List<String[]> pengeluaran = laporan.getTransaksiData(false);
        for (String[] trans : pengeluaran) {
            data.add(new TransactionData(
                    "Pengeluaran",
                    trans[0], // amount
                    trans[1], // category
                    trans[2], // date
                    trans[3]  // time
            ));
        }

        // Create columns
        TableColumn<Transaksi, String> typeCol = new TableColumn<>("Jenis");
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));

        TableColumn<Transaksi, String> amountCol = new TableColumn<>("Jumlah");
        amountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));

        TableColumn<Transaksi, String> categoryCol = new TableColumn<>("Kategori");
        categoryCol.setCellValueFactory(new PropertyValueFactory<>("category"));

        TableColumn<Transaksi, String> dateCol = new TableColumn<>("Tanggal");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));

        TableColumn<Transaksi, String> timeCol = new TableColumn<>("Waktu");
        timeCol.setCellValueFactory(new PropertyValueFactory<>("time"));

        table.getColumns().addAll(typeCol, amountCol, categoryCol, dateCol, timeCol);
        table.setItems(data);

        return table;
    }

    private BarChart<String, Number> createMonthlyChart() throws IOException {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle("Ringkasan Bulanan");

        XYChart.Series<String, Number> incomeSeries = new XYChart.Series<>();
        incomeSeries.setName("Pemasukan");

        XYChart.Series<String, Number> expenseSeries = new XYChart.Series<>();
        expenseSeries.setName("Pengeluaran");

        Map<String, double[]> monthlyData = laporan.getMonthlySummary();
        for (Map.Entry<String, double[]> entry : monthlyData.entrySet()) {
            String month = entry.getKey();
            double income = entry.getValue()[0];
            double expense = entry.getValue()[1];

            incomeSeries.getData().add(new XYChart.Data<>(month, income));
            expenseSeries.getData().add(new XYChart.Data<>(month, expense));
        }

        barChart.getData().addAll(incomeSeries, expenseSeries);
        return barChart;
    }

    public static class TransactionData {
        private final String type;
        private final String amount;
        private final String category;
        private final String date;
        private final String time;

        public TransactionData(String type, String amount, String category, String date, String time) {
            this.type = type;
            this.amount = amount;
            this.category = category;
            this.date = date;
            this.time = time;
        }

        // Getters
        public String getType() { return type; }
        public String getAmount() { return amount; }
        public String getCategory() { return category; }
        public String getDate() { return date; }
        public String getTime() { return time; }
    }
}