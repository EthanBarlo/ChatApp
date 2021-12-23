package com.example.client;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.beans.value.ChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.event.ActionEvent;
import javafx.scene.text.Text;
import javafx.geometry.Insets;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import java.io.IOException;
import java.util.ArrayList;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;
import java.util.HashMap;
import javafx.fxml.FXML;
import java.util.List;
import java.util.Map;
import java.io.File;

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
    private TableColumn<Message, Pane> MessagesColumn;

    @FXML
    private TableView<Message> MessagesTable;

    @FXML
    private BorderPane rootPane;

    Thread updateChecker = new Thread(new Runnable() {
        @Override
        public void run() {
            while(true){
                UpdateChannelContents();
            }
        }
    });

    @FXML
    void initialize() {
        ChannelListView.setItems(channelNames);
        ChannelListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                selectedChannel = t1;
                if(selectedChannel == null){
                    updateChecker.interrupt();
                    return;
                }
                Channel channel = mapOfChannels.get(selectedChannel);
                channel.GetNewMessages(server);
                ObservableList<Message> messages = channel.getMessages();

                MessagesTable.setItems(messages);
                MessagesColumn.setCellValueFactory(data -> new SimpleObjectProperty<>(generateMessagePane(data.getValue())));

                updateChecker.start();
            }
        });
    }

    Pane generateMessagePane(Message message){
        Pane pane = new Pane();
        VBox vbox = new VBox();
        Text text = new Text(message.getFrom() + ": " +message.getBody());

        vbox.setPadding(new Insets(5, 15, 5, 15));
        if(message.getFrom().equals(server.getUsername())){
            vbox.getStyleClass().add("messageContainerUser");
            text.getStyleClass().add("messageTextUser");
        }
        else{
            vbox.getStyleClass().add("messageContainerOther");
            text.getStyleClass().add("messageTextOther");
        }
        vbox.getChildren().add(text);
        pane.getChildren().add(vbox);

        if(message.getFile() != null){
            String[] splitted = message.getBody().split("\\.");
            String fileExtension = splitted[1];
            if(!fileExtension.equals("jpg"))
                return pane;
            Image image = new Image(message.getFile().toURI().toString());
            ImageView imageView = new ImageView();
            imageView.setImage(image);
            imageView.setPreserveRatio(true);
            imageView.setFitWidth(800);
            if(imageView.getFitHeight() > 500)
                imageView.setFitHeight(500);
            vbox.getChildren().add(imageView);
        }
        return pane;
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
            window.setResizable(false);
            window.show();
        }catch(IOException ie){
            ie.printStackTrace();
        }
    }

    @FXML
    void RemoveChannel(ActionEvent event) {
        String channelName = selectedChannel;
        Response response = server.SendRequest(new UnsubscribeRequest(server.getUsername(), channelName));
        if(!(response instanceof SuccessResponse))
            return;
        mapOfChannels.remove(channelName);
        channelNames.remove(channelName);
    }

    @FXML
    void AttachFile(ActionEvent event) {
        if(selectedChannel == null)
            return;

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select file to attach");
        File file = fileChooser.showOpenDialog((Stage)((Node)event.getSource()).getScene().getWindow());
        
        Message message = new Message(server.getUsername(), System.currentTimeMillis() / 1000L, file.getName(), file);
        Response response = server.SendRequest(new PublishRequest(server.getUsername(), selectedChannel, message));

        if(!(response instanceof SuccessResponse))
            return;

        mapOfChannels.get(selectedChannel).GetNewMessages(server);
    }   

    @FXML
    void SendMessage(ActionEvent event) {
        String messageText = MessageInput.getText();
        Message messageData = new Message(server.getUsername(), System.currentTimeMillis() / 1000L, messageText);
        Response response = server.SendRequest(new PublishRequest(server.getUsername(), selectedChannel, messageData));

        if(!(response instanceof SuccessResponse))
            return;

        MessageInput.setText("");
        mapOfChannels.get(selectedChannel).GetNewMessages(server);
    }

    @FXML
    void SetThemeDark(ActionEvent event) {
        server.setCurrentTheme("darkTheme");
        rootPane.getStylesheets().remove(1);
        rootPane.getStylesheets().add(getClass().getResource("Styles/darkTheme.css").toExternalForm());
    }

    @FXML
    void SetThemeLight(ActionEvent event) {
        server.setCurrentTheme("lightTheme");
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
                List<Message> messages = new ArrayList<>();
                mapOfChannels.put(channel, new Channel(messages, channel));
            }
        });
        channelNames.addAll(((StringListResponse) response).getData());
    }

    public void UpdateChannelContents(){
        try{
            mapOfChannels.get(selectedChannel).GetNewMessages(server);
            Thread.sleep(3000);
        }catch (InterruptedException ie){
            ie.printStackTrace();
        }
    }
}
