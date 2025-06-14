package com.example.fttry;

import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class MainApp extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
            // Initialize database
            DatabaseHelper.getInstance();

            primaryStage.setTitle("Financial Tracker");
            primaryStage.setScene(Home.getInstance().getScene());
            primaryStage.setMinWidth(900);
            primaryStage.setMinHeight(650);
            primaryStage.show();
        } catch (Exception e) {
            showErrorAlert("Failed to start application", e.getMessage());
        }
    }

    public static void main(String[] args) {
        try {
            // Add shutdown hook to close database connection
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                DatabaseHelper.getInstance().closeConnection();
            }));

            launch(args);
        } catch (Exception e) {
            showErrorAlert("Application Error", e.getMessage());
        }
    }

    private static void showErrorAlert(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}