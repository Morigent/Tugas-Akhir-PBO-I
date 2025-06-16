package com.example.fttry;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.TreeMap;

public class IncomeExpenseComparisonChart {
    public static void showChart(Map<LocalDate, Double> income, Map<LocalDate, Double> expenses) {
        Platform.runLater(() -> {
            Stage stage = new Stage();
            stage.setTitle("Perbandingan Pemasukkan dan Pengeluaran");

            // Create axes
            CategoryAxis xAxis = new CategoryAxis();
            NumberAxis yAxis = new NumberAxis();

            // Create line chart
            LineChart<String, Number> lineChart = new LineChart<>(xAxis, yAxis);
            lineChart.setTitle("Perbandingan Bulanan Pemasukkan dan Pengeluaran");
            xAxis.setLabel("Bulan/Tahun");
            yAxis.setLabel("Jumlah (Rp)");

            // Create series
            XYChart.Series<String, Number> incomeSeries = new XYChart.Series<>();
            incomeSeries.setName("Pemasukkan");

            XYChart.Series<String, Number> expenseSeries = new XYChart.Series<>();
            expenseSeries.setName("Pengeluaran");

            // Combine all dates to ensure all months are shown
            TreeMap<LocalDate, Double> allDates = new TreeMap<>();
            allDates.putAll(income);
            allDates.putAll(expenses);

            // Format for displaying dates
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM yyyy");

            // Add data to series
            for (LocalDate date : allDates.keySet()) {
                String monthYear = date.format(formatter);
                Double incomeAmount = income.getOrDefault(date, 0.0);
                Double expenseAmount = expenses.getOrDefault(date, 0.0);

                incomeSeries.getData().add(new XYChart.Data<>(monthYear, incomeAmount));
                expenseSeries.getData().add(new XYChart.Data<>(monthYear, expenseAmount));
            }

            // Add series to chart
            lineChart.getData().addAll(incomeSeries, expenseSeries);

            // Customize appearance
            lineChart.setCreateSymbols(true);
            lineChart.setLegendVisible(true);
            lineChart.setAnimated(false);

            // Show the stage
            stage.setScene(new Scene(lineChart, 800, 600));
            stage.show();
        });
    }
}