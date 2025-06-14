package com.example.fttry;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Optional;
import java.sql.*;

public class Home {
    private static final Home instance = new Home();
    private final StackPane root;
    private final Scene scene;
    private final Label saldoLabel;
    private final TableView<Transaksi> table;
    private final ObservableList<Transaksi> data;
    private final TransactionService transactionService;
    private MediaPlayer mediaPlayer;
    private MediaView mediaView;

    public Home() {
        transactionService = new TransactionService();
        root = new StackPane();
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-padding: 20;");


        initVideoBackground(root);

        // Header Section
        Label title = new Label("Financial Tracker");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #ededed; -fx-font-family: poppins bold;");

        saldoLabel = new Label("Saldo saat ini: Rp.0");
        saldoLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #ededed;" +
                "-fx-font-family: poppins semi bold;");

        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER);
        Button incomeBtn = createStyledButton("Pemasukkan");
        Button expenseBtn = createStyledButton("Pengeluaran");
        Button filterBtn = createStyledButton("Filter");


        buttonBox.getChildren().addAll(incomeBtn, expenseBtn, filterBtn);

        VBox headerBox = new VBox(15, title, saldoLabel, buttonBox);
        headerBox.setStyle("-fx-background-color: #282829; -fx-background-radius: 20; -fx-padding: 20;");
        headerBox.setAlignment(Pos.CENTER);

        // Transaction Table
        table = new TableView<>();
        data = FXCollections.observableArrayList();
        setupTableColumns();
        setupContextMenu();
        table.setStyle("-fx-text-fill: #ededed; -fx-background-color: #282829; -fx-border-color: #8a8a8a; -fx-border-width: 2px;");

        VBox tableBox = new VBox(10, table);
        tableBox.setStyle("-fx-background-color: #282829; -fx-background-radius: 20; -fx-padding: 20;");
        tableBox.setMaxHeight(400);

        VBox contentContainer = new VBox(20);
        contentContainer.setAlignment(Pos.CENTER);
        contentContainer.getChildren().addAll(headerBox, tableBox);

        root.getChildren().addAll(contentContainer);
        scene = new Scene(root, 900, 650);

        // Load data
        refreshData();

        // Button actions
        incomeBtn.setOnAction(e -> showTransactionDialog("Pemasukkan", null));
        expenseBtn.setOnAction(e -> showTransactionDialog("Pengeluaran", null));
        filterBtn.setOnAction(e -> showFilterDialog());
    }

    private void initVideoBackground(StackPane root) {
        try {
            URL videoUrl = getClass().getResource("/com/example/fttry/videos/background.mp4");
            System.out.println("Video exists: " + (videoUrl != null));
            System.out.println("Video URI: " + (videoUrl != null ? videoUrl.toURI() : "null"));

            if (videoUrl != null) {
                File videoFile = new File(videoUrl.toURI());
                System.out.println("File size: " + videoFile.length() + " bytes");
                System.out.println("Can read: " + videoFile.canRead());
            }

            InputStream videoStream = getClass().getResourceAsStream("/com/example/fttry/videos/background.mp4");
            File tempFile = File.createTempFile("fxvideo", ".mp4");
            tempFile.deleteOnExit();
            Files.copy(videoStream, tempFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

            Media media = new Media(tempFile.toURI().toString());
            MediaPlayer mediaPlayer = new MediaPlayer(media);
            MediaView mediaView = new MediaView(mediaPlayer);

            // Detailed error handling
            mediaPlayer.setOnError(() -> {
                System.err.println("Media Error Details:");
                System.err.println("Message: " + mediaPlayer.getError().getMessage());
                System.err.println("Media Source: " + media.getSource());
                showFallbackBackground(root);
            });

            mediaPlayer.setOnReady(() -> {
                System.out.println("Video Metadata:");
                System.out.println("Duration: " + media.getDuration().toSeconds() + "s");
                System.out.println("Dimensions: " + media.getWidth() + "x" + media.getHeight());
                mediaPlayer.play();
            });

            mediaView.setPreserveRatio(false);
            mediaView.fitWidthProperty().bind(root.widthProperty());
            mediaView.fitHeightProperty().bind(root.heightProperty());

            root.getChildren().add(0, mediaView);
            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            mediaPlayer.setMute(true);

        } catch (Exception e) {
            System.err.println("Video initialization failed: " + e.getClass().getSimpleName() + ": " + e.getMessage());
            showFallbackBackground(root);
        }
    }

    private void showFallbackBackground(StackPane root) {
        try {
            // Try CSS gradient
            root.setStyle("-fx-background-color: linear-gradient(to bottom, #547792, #213448);");

            // OR: Use an image fallback
            ImageView fallback = new ImageView(
                    new Image(getClass().getResourceAsStream("/com/example/fttry/images/fallback.jpg")));
            root.getChildren().add(0, fallback);
        } catch (Exception e) {
            // Ultimate fallback
            root.setStyle("-fx-background-color: #547792;");
        }
    }

    private Button createStyledButton(String text) {
        Button button = new Button(text);
        button.setStyle("-fx-font-weight: bold; -fx-text-fill: #282829; -fx-font-family: poppins semi bold;" +
                "-fx-background-color: #ededed;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 5, 0, 0, 1);");
        return button;
    }

    private void setupTableColumns() {
        // Type Column
        TableColumn<Transaksi, String> typeCol = new TableColumn<>("Jenis");
        typeCol.setCellValueFactory(new PropertyValueFactory<>("jenis"));
        typeCol.setPrefWidth(100);

        // Amount Column
        TableColumn<Transaksi, Double> amountCol = new TableColumn<>("Jumlah");
        amountCol.setCellValueFactory(new PropertyValueFactory<>("jumlah"));
        amountCol.setCellFactory(tc -> new TableCell<>() {
            @Override
            protected void updateItem(Double amount, boolean empty) {
                super.updateItem(amount, empty);
                if (empty || amount == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(formatCurrency(amount));
                    if (getTableRow() != null && getTableRow().getItem() != null) {
                        setTextFill(getTableRow().getItem().getJenis().equals("Pemasukkan") ?
                                Color.GREEN : Color.RED);
                    }
                }
            }
        });
        amountCol.setPrefWidth(150);

        // Category Column
        TableColumn<Transaksi, String> categoryCol = new TableColumn<>("Kategori");
        categoryCol.setCellValueFactory(new PropertyValueFactory<>("kategori"));
        categoryCol.setPrefWidth(150);

        // Description Column
        TableColumn<Transaksi, String> descCol = new TableColumn<>("Deskripsi");
        descCol.setCellValueFactory(new PropertyValueFactory<>("deskripsi"));
        descCol.setPrefWidth(200);

        // Date Column
        TableColumn<Transaksi, LocalDate> dateCol = new TableColumn<>("Tanggal");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("tanggal"));
        dateCol.setCellFactory(tc -> new TableCell<>() {
            private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

            @Override
            protected void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setText(empty || date == null ? null : date.format(formatter));
            }
        });
        dateCol.setPrefWidth(120);

        table.getColumns().addAll(typeCol, amountCol, categoryCol, descCol, dateCol);
        table.setItems(data);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private void setupContextMenu() {
        table.setRowFactory(tv -> {
            TableRow<Transaksi> row = new TableRow<>();
            ContextMenu contextMenu = new ContextMenu();

            MenuItem editItem = new MenuItem("Edit");
            editItem.setOnAction(event -> {
                if (!row.isEmpty()) {
                    showTransactionDialog(row.getItem().getJenis(), row.getItem());
                }
            });

            MenuItem deleteItem = new MenuItem("Delete");
            deleteItem.setOnAction(event -> {
                if (!row.isEmpty()) {
                    deleteTransaction(row.getItem());
                }
            });

            contextMenu.getItems().addAll(editItem, deleteItem);
            row.contextMenuProperty().bind(
                    Bindings.when(row.emptyProperty())
                            .then((ContextMenu)null)
                            .otherwise(contextMenu)
            );
            return row;
        });
    }

    private void showTransactionDialog(String type, Transaksi existingTransaction) {
        Dialog<Transaksi> dialog = new Dialog<>();
        dialog.setTitle(existingTransaction == null ? "Tambah " + type : "Edit Transaksi");

        ButtonType saveButtonType = new ButtonType("Simpan", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setStyle("-fx-background-color: #282829");
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 10, 10, 10));

        TextField amountField = new TextField();
        amountField.setPromptText("Jumlah");
        amountField.setStyle("-fx-text-fill: #ededed; -fx-background-color: #282829; -fx-border-color: #8a8a8a; -fx-border-width: 2px;");
        TextField categoryCombo = new TextField();
        categoryCombo.setPromptText("Gaji");
        categoryCombo.setStyle("-fx-text-fill: #ededed; -fx-background-color: #282829; -fx-border-color: #8a8a8a; -fx-border-width: 2px;");
        TextArea descriptionArea = new TextArea();
        descriptionArea.setPromptText("Deskripsi");
        descriptionArea.setPrefRowCount(3);
        descriptionArea.setStyle("-fx-control-inner-background: #282829; -fx-text-fill: #ededed; -fx-border-color: #8a8a8a; -fx-border-width: 2px;");
        DatePicker datePicker = new DatePicker();
        datePicker.setStyle("-fx-text-fill: #ededed; -fx-background-color: #282829; -fx-border-color: #8a8a8a; -fx-border-width: 2px;");


        if (existingTransaction != null) {
            amountField.setText(String.valueOf(existingTransaction.getJumlah()));
            descriptionArea.setText(existingTransaction.getDeskripsi());
            datePicker.setValue(existingTransaction.getTanggal());
        } else {
            datePicker.setValue(LocalDate.now());
        }

        Label jumlah= new Label("Jumlah");
        jumlah.setStyle("-fx-text-fill: #ededed;");
        Label kategori = new Label("Kategori");
        kategori.setStyle("-fx-text-fill: #ededed;");
        Label deskripsi = new Label("Deskripsi");
        deskripsi.setStyle("-fx-text-fill: #ededed;");
        Label tanggal = new Label("Tanggal");
        tanggal.setStyle("-fx-text-fill: #ededed;");

        grid.add(jumlah, 0, 0);
        grid.add(amountField, 1, 0);
        grid.add(kategori, 0, 1);
        grid.add(categoryCombo, 1, 1);
        grid.add(deskripsi, 0, 2);
        grid.add(descriptionArea, 1, 2);
        grid.add(tanggal, 0, 3);
        grid.add(datePicker, 1, 3);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(buttonType -> {
            if (buttonType == saveButtonType) {
                try {
                    if (!InputValidator.validateTransaction(amountField.getText(), categoryCombo.getText())) {
                        showAlert(Alert.AlertType.ERROR, "Input Tidak Valid",
                                "Jumlah harus positif dan kategori harus diisi");
                        return null;
                    }

                    double amount = Double.parseDouble(amountField.getText());
                    String category = categoryCombo.getText();
                    String description = descriptionArea.getText();
                    LocalDate date = datePicker.getValue();

                    if (existingTransaction == null) {
                        return new Transaksi(type, amount, category, description, date);
                    } else {
                        existingTransaction.setJumlah(amount);
                        existingTransaction.setKategori(category);
                        existingTransaction.setDeskripsi(description);
                        existingTransaction.setTanggal(date);
                        return existingTransaction;
                    }
                } catch (NumberFormatException e) {
                    showAlert(Alert.AlertType.ERROR, "Error", "Jumlah harus berupa angka");
                    return null;
                }
            }
            return null;
        });

        Optional<Transaksi> result = dialog.showAndWait();
        result.ifPresent(transaksi -> {
            try {
                if (existingTransaction == null) {
                    transactionService.addTransaction(transaksi);
                } else {
                    transactionService.updateTransaction(transaksi);
                }
                refreshData();
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Database Error",
                        "Gagal menyimpan transaksi: " + e.getMessage());
            }
        });
    }

    private void showFilterDialog() {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Filter Transaksi");

        ButtonType applyButtonType = new ButtonType("Terapkan", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(applyButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setStyle("-fx-background-color: #282829");
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 10, 10, 10));

        ComboBox<String> typeCombo = new ComboBox<>();
        typeCombo.getItems().addAll("Semua", "Pemasukkan", "Pengeluaran");
        typeCombo.setValue("Semua");

        ComboBox<String> categoryCombo = new ComboBox<>();
        categoryCombo.getItems().add("Semua");
        try {
            categoryCombo.getItems().addAll(transactionService.getCategories());
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Gagal memuat kategori");
        }
        categoryCombo.setValue("Semua");

        DatePicker fromDatePicker = new DatePicker();
        DatePicker toDatePicker = new DatePicker(LocalDate.now());

        Label jenis= new Label("Jenis: ");
        jenis.setStyle("-fx-text-fill: #ededed;");
        Label kategori = new Label("Kategori: ");
        kategori.setStyle("-fx-text-fill: #ededed;");
        Label dariTanggal = new Label("dari Tanggal:");
        dariTanggal.setStyle("-fx-text-fill: #ededed;");
        Label sampaiTanggal = new Label("sampai Tanggal: ");
        sampaiTanggal.setStyle("-fx-text-fill: #ededed;");

        grid.add(jenis, 0, 0);
        grid.add(typeCombo, 1, 0);
        grid.add(kategori, 0, 1);
        grid.add(categoryCombo, 1, 1);
        grid.add(dariTanggal, 0, 2);
        grid.add(fromDatePicker, 1, 2);
        grid.add(sampaiTanggal, 0, 3);
        grid.add(toDatePicker, 1, 3);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(buttonType -> {
            if (buttonType == applyButtonType) {
                try {
                    String type = typeCombo.getValue().equals("Semua") ? null : typeCombo.getValue();
                    String category = categoryCombo.getValue().equals("Semua") ? null : categoryCombo.getValue();
                    LocalDate fromDate = fromDatePicker.getValue();
                    LocalDate toDate = toDatePicker.getValue();

                    applyFilters(type, category, fromDate, toDate);
                } catch (SQLException e) {
                    showAlert(Alert.AlertType.ERROR, "Error", "Gagal menerapkan filter");
                }
            }
            return null;
        });

        dialog.showAndWait();
    }

    private void applyFilters(String type, String category, LocalDate fromDate, LocalDate toDate) throws SQLException {
        StringBuilder sql = new StringBuilder("SELECT * FROM transaksi WHERE 1=1");

        if (type != null) {
            sql.append(" AND jenis = '").append(type).append("'");
        }
        if (category != null) {
            sql.append(" AND kategori = '").append(category).append("'");
        }
        if (fromDate != null) {
            sql.append(" AND tanggal >= '").append(fromDate).append("'");
        }
        if (toDate != null) {
            sql.append(" AND tanggal <= '").append(toDate).append("'");
        }

        sql.append(" ORDER BY tanggal DESC, id DESC");

        data.clear();
        try (Connection conn = DatabaseHelper.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql.toString())) {

            while (rs.next()) {
                Transaksi transaksi = new Transaksi(
                        rs.getString("jenis"),
                        rs.getDouble("jumlah"),
                        rs.getString("kategori"),
                        rs.getString("deskripsi"),
                        LocalDate.parse(rs.getString("tanggal"))
                );
                transaksi.setId(rs.getInt("id"));
                data.add(transaksi);
            }
        }
        updateBalance();
    }

    private void deleteTransaction(Transaksi transaksi) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Konfirmasi Hapus");
        alert.setHeaderText("Hapus transaksi ini?");
        alert.setContentText("Transaksi: " + transaksi.getKategori() + " - " +
                formatCurrency(transaksi.getJumlah()));

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                transactionService.deleteTransaction(transaksi.getId());
                data.remove(transaksi);
                updateBalance();
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Gagal menghapus transaksi");
            }
        }
    }

    private void refreshData() {
        try {
            data.setAll(transactionService.getAllTransactions());
            updateBalance();
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Gagal memuat data transaksi");
        }
    }

    private void updateBalance() {
        try {
            double balance = transactionService.calculateBalance();
            saldoLabel.setText("Saldo saat ini: " + formatCurrency(balance));
            saldoLabel.setTextFill(balance >= 0 ? Color.GREEN : Color.RED);
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Gagal menghitung saldo");
        }
    }

    private String formatCurrency(double amount) {
        NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
        return format.format(amount).replace("Rp", "Rp.");
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static Home getInstance() {
        return instance;
    }

    public Scene getScene() {
        return scene;
    }
}