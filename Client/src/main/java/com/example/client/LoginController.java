package com.example.client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {
    @FXML
    private Label InvalidText;

    @FXML
    private TextField passwordField;

    @FXML
    private TextField usernameField;

    @FXML
    void LoginClicked(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();
        Server server = new Server("localhost", 12345);
        Response response = server.SendRequest(new OpenRequest(username, password));
        if(!(response instanceof SuccessResponse)){
            InvalidText.setVisible(true);
            server.Close();
            return;
        }

        server.setUsername(username);

        try{
            FXMLLoader fxmlLoader = new FXMLLoader(ClientStart.class.getResource("MainApp.fxml"));
            Parent mainAppParent = fxmlLoader.load();
            MainAppController mainAppController = fxmlLoader.getController();
            mainAppController.Setup(server);

            Scene mainAppScene = new Scene(mainAppParent);
            Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
            window.setScene(mainAppScene);
        }catch (IOException ie){
            ie.printStackTrace();
        }
    }
}