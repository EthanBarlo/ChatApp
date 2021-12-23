package com.example.client;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.ArrayList;

public class AddChannelController {
    private Server server;
    private MainAppController mainAppController;
    @FXML
    private ListView<String> ChannelListView;

    @FXML
    private TextField TextField;

    @FXML
    void initialize() {
        ChannelListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                TextField.setText(t1);
            }
        });
    }

    @FXML
    void Create(ActionEvent event) {
        String channelName = TextField.getText();
        Response response = server.SendRequest(new CreateChannelRequest(channelName));
        if(!(response instanceof SuccessResponse)){
            return;
        }
        Channel channel = new Channel(new ArrayList<>(), channelName);
        mainAppController.AddSubscribedChannel(channel);
        Close();
    }

    @FXML
    void Subscribe(ActionEvent event) {
        String channelName = TextField.getText();

        Response response = server.SendRequest(new SubscribeRequest(server.getUsername(), channelName));
        if(!(response instanceof SuccessResponse)){
            return;
        }
        Channel channel = new Channel(new ArrayList<>(), channelName);
        mainAppController.AddSubscribedChannel(channel);
        Close();
    }

    private void Close(){
        ((Stage)TextField.getScene().getWindow()).close();
    }

    void Setup(Server server, MainAppController mainAppController){
        this.server = server;
        this.mainAppController = mainAppController;
        Response response = server.SendRequest(new GetChannelsRequest("all"));
        if(!(response instanceof StringListResponse)){
            TextField.setText("Error, list could not load.");
            return;
        }
        ObservableList<String> listOfChannels = FXCollections.observableList(((StringListResponse) response).getData());
        ChannelListView.setItems(listOfChannels);
    }
}