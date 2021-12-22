package com.example.client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;

import java.net.URL;

public class MainAppController {
    private Server server;

    @FXML
    private ListView<?> ChannelListView;

    @FXML
    private TextArea MessageInput;

    @FXML
    private TableColumn<?, ?> MessagesColumn;

    @FXML
    private TableView<?> MessagesTable;

    @FXML
    private BorderPane rootPane;

    @FXML
    void AddChannel(ActionEvent event) {

    }

    @FXML
    void AttachFile(ActionEvent event) {

    }

    @FXML
    void SendMessage(ActionEvent event) {
        server.SendRequest(new OpenRequest("asdasd", "asdasd"));
    }

    @FXML
    void SetThemeDark(ActionEvent event) {
        rootPane.getStylesheets().remove(1);
        rootPane.getStylesheets().add(getClass().getResource("Styles/darkTheme.css").toExternalForm());
    }

    @FXML
    void SetThemeLight(ActionEvent event) {
        rootPane.getStylesheets().remove(1);
        rootPane.getStylesheets().add(getClass().getResource("Styles/lightTheme.css").toExternalForm());
    }

    public void SetServer(Server server){
        this.server = server;
    }
}
