package com.example.fttry;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Map;

public class ChartView extends Application {

    public static Map<String, Integer> data; // GUNAKAN INI SAJA

    public ChartView() {} // tidak perlu konstruktor dengan parameter

    @Override
    public void start(Stage stage) {
        stage.setTitle("Grafik Transaksi");

        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);

        xAxis.setLabel("Kategori");
        yAxis.setLabel("Jumlah Transaksi");

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Jumlah Transaksi per Kategori");

        for (Map.Entry<String, Integer> entry : data.entrySet()) {
            series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }

        barChart.getData().add(series);

        VBox vbox = new VBox(barChart);
        Scene scene = new Scene(vbox, 600, 400);
        stage.setScene(scene);
        stage.show();
    }
}
