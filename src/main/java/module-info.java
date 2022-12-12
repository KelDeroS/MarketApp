module com.example.marketapp {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;

    opens com.example.marketapp to javafx.fxml;
    exports com.example.marketapp;
}