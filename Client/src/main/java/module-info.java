module com.example.client {
    requires javafx.controls;
    requires javafx.fxml;
    requires json.simple;


    opens com.example.client to javafx.fxml;
    exports com.example.client;
}