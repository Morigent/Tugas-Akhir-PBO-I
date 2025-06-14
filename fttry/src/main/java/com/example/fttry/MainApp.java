package com.example.fttry;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class MainApp extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
            // Initialize database
            DatabaseHelper.getInstance();

            // Setup main window
            primaryStage.setTitle("Financial Tracker");
            primaryStage.setScene(Home.getInstance().getScene());
            primaryStage.setMinWidth(900);
            primaryStage.setMinHeight(650);
            primaryStage.show();

            // Close database connection when application exits
            primaryStage.setOnCloseRequest(e -> {
                DatabaseHelper.getInstance().closeConnection();
            });
        } catch (Exception e) {
            e.printStackTrace();
            showErrorAlert("Failed to start application", e.getMessage());
        }
    }

    public static void main(String[] args) {
        try {
            Class.forName("org.sqlite.JDBC");
            launch(args);
        } catch (ClassNotFoundException e) {
            // Use Platform.runLater to show error on FX thread
            Platform.runLater(() ->
                    showErrorAlert("SQLite JDBC driver not found", e.getMessage())
            );
            e.printStackTrace();
        }
    }

    private static void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}