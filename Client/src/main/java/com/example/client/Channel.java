package com.example.client;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

public class Channel {
    private ObservableList<Message> messages;
    private String name;

    public Channel(List<Message> messages, String name){
        this.messages = FXCollections.observableList(messages);
        this.name = name;
    }

    public ObservableList<Message> getMessages() {return messages;}
    public String getName() {return name;}

    public void AddNewMessages(List<Message> newMessages){
        messages.addAll(newMessages);
    }

    public void GetNewMessages(Server server){
        Response response = server.SendRequest(new GetRequest(server.getUsername(), name, messages.size()));
        if(!(response instanceof MessageListResponse))
            return;
        messages.addAll(((MessageListResponse) response).getMessages());
    }
}
