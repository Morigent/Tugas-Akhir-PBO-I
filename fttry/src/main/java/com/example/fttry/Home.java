package com.example.fttry;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Optional;
import java.sql.*;

public class Home {
    private static final Home instance = new Home();
    private final VBox root;
    private final Scene scene;
    private final Label saldoLabel;
    private final TableView<Transaksi> table;
    private final ObservableList<Transaksi> data;
    private final TransactionService transactionService;

    public Home() {
        transactionService = new TransactionService();
        root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: #547792; -fx-padding: 20;");
        scene = new Scene(root, 900, 650);

        // Header Section
        Label title = new Label("Financial Tracker");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #ECEFCA;");

        try {
            saldoLabel = new Label("Saldo saat ini: Rp." + formatCurrency(transactionService.calculateBalance()));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        saldoLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #ECEFCA;");

        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER);
        Button incomeBtn = createStyledButton("Pemasukkan");
        Button expenseBtn = createStyledButton("Pengeluaran");
        Button filterBtn = createStyledButton("Filter");
        buttonBox.getChildren().addAll(incomeBtn, expenseBtn, filterBtn);

        VBox headerBox = new VBox(15, title, saldoLabel, buttonBox);
        headerBox.setStyle("-fx-background-color: #213448; -fx-background-radius: 20; -fx-padding: 20;");
        headerBox.setAlignment(Pos.CENTER);

        // Transaction Table
        table = new TableView<>();
        data = FXCollections.observableArrayList();
        setupTableColumns();
        setupContextMenu();

        VBox tableBox = new VBox(10, table);
        tableBox.setStyle("-fx-background-color: #213448; -fx-background-radius: 20; -fx-padding: 20;");
        tableBox.setMaxHeight(400);

        root.getChildren().addAll(headerBox, tableBox);

        // Load data
        refreshData();

        // Button actions
        incomeBtn.setOnAction(e -> showTransactionDialog("Pemasukkan", null));
        expenseBtn.setOnAction(e -> showTransactionDialog("Pengeluaran", null));
        filterBtn.setOnAction(e -> showFilterDialog());
    }

    private Button createStyledButton(String text) {
        Button button = new Button(text);
        button.setStyle("-fx-background-radius: 20; -fx-padding: 10 20; " +
                "-fx-background-color: #71C0BB; -fx-font-weight: bold; " +
                "-fx-text-fill: #332D56;");
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
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 10, 10, 10));

        TextField amountField = new TextField();
        amountField.setPromptText("dalam rupiah");
        TextField categoryField = new TextField();
        categoryField.setPromptText("Gaji/Bonus/dll...");
        TextArea descriptionArea = new TextArea();
        descriptionArea.setPromptText("Endorse an");
        descriptionArea.setPrefRowCount(3);
        DatePicker datePicker = new DatePicker();

        if (existingTransaction != null) {
            amountField.setText(String.valueOf(existingTransaction.getJumlah()));
            descriptionArea.setText(existingTransaction.getDeskripsi());
            datePicker.setValue(existingTransaction.getTanggal());
        } else {
            datePicker.setValue(LocalDate.now());
        }


        grid.add(new Label("Jumlah:"), 0, 0);
        grid.add(amountField, 1, 0);
        grid.add(new Label("Kategori:"), 0, 1);
        grid.add(categoryField, 1, 1);
        grid.add(new Label("Deskripsi:"), 0, 2);
        grid.add(descriptionArea, 1, 2);
        grid.add(new Label("Tanggal:"), 0, 3);
        grid.add(datePicker, 1, 3);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(buttonType -> {
            if (buttonType == saveButtonType) {
                try {
                    if (!InputValidator.validateTransaction(amountField.getText(), categoryField.getText())) {
                        showAlert(Alert.AlertType.ERROR, "Input Tidak Valid",
                                "Jumlah harus positif dan kategori harus diisi");
                        return null;
                    }

                    double amount = Double.parseDouble(amountField.getText());
                    String category = categoryField.getText();
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

        grid.add(new Label("Jenis:"), 0, 0);
        grid.add(typeCombo, 1, 0);
        grid.add(new Label("Kategori:"), 0, 1);
        grid.add(categoryCombo, 1, 1);
        grid.add(new Label("Dari Tanggal:"), 0, 2);
        grid.add(fromDatePicker, 1, 2);
        grid.add(new Label("Sampai Tanggal:"), 0, 3);
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