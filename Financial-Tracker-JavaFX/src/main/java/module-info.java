module com.example.financialtrackerjavafx {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;

    opens com.example.financialtrackerjavafx to javafx.fxml;
    exports com.example.financialtrackerjavafx;
    exports com.example.financialtrackerjavafx;
    opens com.example.financialtrackerjavafx to javafx.fxml;
    exports com.example.financialtrackerjavafx.Transaksi;
    opens com.example.financialtrackerjavafx.Transaksi to javafx.fxml;
    exports com.example.financialtrackerjavafx.Menu;
    opens com.example.financialtrackerjavafx.Menu to javafx.fxml;
}