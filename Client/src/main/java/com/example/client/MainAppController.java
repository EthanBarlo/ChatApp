package com.example.client;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainAppController {
    private Server server;

    @FXML
    private ListView<String> ChannelListView;
    private Map<String, Channel> mapOfChannels = new HashMap<>();
    private ObservableList<String> channelNames = FXCollections.observableList(new ArrayList<>());
    private String selectedChannel;
    @FXML
    private TextArea MessageInput;

    @FXML
    private TableColumn<Message, String> MessagesColumn;

    @FXML
    private TableView<Message> MessagesTable;

    @FXML
    private BorderPane rootPane;

    @FXML
    void initialize() {
        ChannelListView.setItems(channelNames);
        ChannelListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                selectedChannel = t1;
                Channel channel = mapOfChannels.get(selectedChannel);
                channel.GetNewMessages(server);
                ObservableList<Message> messages = channel.getMessages();

                MessagesTable.setItems(messages);
                MessagesColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getBody()));
            }
        });
    }

    @FXML
    void AddChannel(ActionEvent event) {
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(ClientStart.class.getResource("AddChannel.fxml"));
            Parent parent = fxmlLoader.load();
            AddChannelController addChannelController = fxmlLoader.getController();
            addChannelController.Setup(server, this);

            Scene addChannel = new Scene(parent);
            Stage window = new Stage();
            window.setTitle("Add Channel");
            window.setScene(addChannel);
            window.show();
        }catch(IOException ie){
            ie.printStackTrace();
        }
    }

    @FXML
    void AttachFile(ActionEvent event) {

    }

    @FXML
    void SendMessage(ActionEvent event) {
        String messageText = MessageInput.getText();
        Message messageData = new Message(server.getUsername(), System.currentTimeMillis() / 1000L, messageText);
        Response response = server.SendRequest(new PublishRequest(server.getUsername(), selectedChannel, messageData));

        if(!(response instanceof SuccessResponse)){
            return;
        }
        mapOfChannels.get(selectedChannel).GetNewMessages(server);
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


    public void AddSubscribedChannel(Channel channel){
        mapOfChannels.put(channel.getName(), channel);
        channelNames.add(channel.getName());
    }

    public void Setup(Server server){
        this.server = server;

        Response response = server.SendRequest(new GetChannelsRequest("subscribed"));
        if(!(response instanceof StringListResponse)){
            return;
        }
        List<String> channelList = ((StringListResponse) response).getData();
        channelList.forEach(channel -> {
            if(mapOfChannels.get(channel) == null){
                List<Message> messages;
                Response messageList = server.SendRequest(new GetRequest(server.getUsername(), channel, 0));

                if(messageList instanceof MessageListResponse)
                    messages = ((MessageListResponse) messageList).getMessages();
                else
                    messages = new ArrayList<>();

                mapOfChannels.put(channel, new Channel(messages, channel));
            }
        });
        channelNames.addAll(((StringListResponse) response).getData());
    }

    public void CheckForUpdates(){
        while(true){
            // TODO: 23/12/2021 LOVE THIS HAVE FUN, Check whats new and get it... SEE pretty simple am i right!
        }
    }
}
