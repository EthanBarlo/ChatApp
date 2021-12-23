package com.example.client;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Server{
    private boolean IsInUse = false;

    private Socket socket;
    private PrintWriter _ToServer;
    private BufferedReader _FromServer;

    private String username;
    public String getUsername() {return username;}
    public void setUsername(String username) {this.username = username;}

    public Server(String hostname, int port){
        try{
            socket = new Socket(hostname, port);
            _ToServer = new PrintWriter(socket.getOutputStream(), true);
            _FromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        }catch(IOException ie){
            ie.printStackTrace();
        }
    }

    public Response SendRequest(Request request){
        if(IsInUse) return null;
        IsInUse = true;

        System.out.println("REQUEST - " + request.toJSONString());
        _ToServer.println(request.toJSONString());
        String rawResponse = null;
        try {
            rawResponse = _FromServer.readLine();
        }catch (IOException ie){
            ie.printStackTrace();
        }
        System.out.println("RESPONSE - " + rawResponse);

        IsInUse = false;
        if(rawResponse == null) return null;

        JSONObject jsonObject = (JSONObject) JSONValue.parse(rawResponse);
        switch (jsonObject.get("_class").toString()){
            case "SuccessResponse" -> {return SuccessResponse.fromJSON(jsonObject);}
            case "ErrorResponse" -> {return ErrorResponse.fromJSON(jsonObject);}
            case "MessageListResponse" -> {return MessageListResponse.fromJSON(jsonObject);}
            case "StringListResponse" -> {return StringListResponse.fromJSON(jsonObject);}
            default -> {return new ErrorResponse("Response was class - " + jsonObject.get("_class"));}
        }

    }

    public void Close(){
        try{
            socket.close();
        }catch (IOException ie){
            ie.printStackTrace();
        }
    }
}
