package com.example.client;

import org.json.simple.JSONObject;

public class GetRequest extends  Request{
    private static final String _class = GetRequest.class.getSimpleName();
    private final String identity; // Client
    private final String from; // Channel
    private final int after; // index Of Last Received Message

    public GetRequest(String identity, String from, int after){
        this.identity = identity;
        this.from = from;
        this.after = after;
    }

    public String getIdentity() { return identity; }
    public String getFrom() {return from;}
    public int getAfter() {return after;}

    @SuppressWarnings("unchecked")
    public Object toJSON(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("_class", _class);
        jsonObject.put("identity", identity);
        jsonObject.put("from", from);
        jsonObject.put("after", after);
        return jsonObject;
    }

    // TODO: 22/12/2021 REMOVE THIS
    public static GetRequest fromJSON(JSONObject jsonObject){
        String identity = (String) jsonObject.get("identity");
        String from = (String) jsonObject.get("from");
        int after = (int) jsonObject.get("after");
        return new GetRequest(identity, from, after);
    }
}
