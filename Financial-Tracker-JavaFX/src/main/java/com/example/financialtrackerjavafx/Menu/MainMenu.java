package com.example.financialtrackerjavafx.Menu;

import com.example.financialtrackerjavafx.Transaksi.Pemasukan;
import com.example.financialtrackerjavafx.Transaksi.Pengeluaran;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class MainMenu extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        VBox root = new VBox(10);
        root.setAlignment(Pos.CENTER);

        HBox hBox = new HBox(10);
        hBox.setAlignment(Pos.CENTER);

        Button pemasukkan = new Button();
        pemasukkan.setText("Pemasukkan");

        Button pengeluaran = new Button();
        pengeluaran.setText("pengeluaran");

        hBox.getChildren().addAll(pemasukkan,pengeluaran);

        root.getChildren().addAll(hBox);

        Scene scene = new Scene(root, 320, 240);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();

        pemasukkan.setOnAction(event -> {
            Pemasukan user = new Pemasukan(0,"");
            user.inputTransaksi();
            stage.close();
        });

        pengeluaran.setOnAction(event -> {
            Pengeluaran user =  new Pengeluaran(0,"");;
            user.inputTransaksi();
            stage.close();
        });

    }

    public static void main(String[] args) {
        launch(args);
    }
}