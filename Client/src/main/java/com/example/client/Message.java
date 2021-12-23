package com.example.client;

import java.io.Serializable;
import org.json.simple.JSONObject;

public class Message implements Serializable {
    private static final String _class = Message.class.getSimpleName();
    private final String from; // Who sent the message
    private final long when;
    private final String body; // Message text

    public Message(String from, long when, String body){
        this.from = from;
        this.when = when;
        this.body = body;
    }
    // TODO: 15/12/2021 ADD IMAGE SENDING!

    public String getFrom(){ return from; }
    public String getBody() { return body; }
    public long getWhen() {return when;}

    @SuppressWarnings("unchecked")
    public String toJSONString(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("_class", _class);
        jsonObject.put("from", from);
        jsonObject.put("when", when);
        jsonObject.put("body", body);
        return jsonObject.toJSONString();
    }

    public static Message fromJSON(JSONObject jsonObject){
        String from = (String) jsonObject.get("from");
        long when = (long) jsonObject.get("when");
        String body = (String) jsonObject.get("body");
        return new Message(from, when, body);
    }
}
