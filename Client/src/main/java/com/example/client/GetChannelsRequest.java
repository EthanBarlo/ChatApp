package com.example.client;

import org.json.simple.JSONObject;

public class GetChannelsRequest extends  Request{
    private static final String _class = GetChannelsRequest.class.getSimpleName();

    @SuppressWarnings("unchecked")
    public Object toJSON(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("_class", _class);
        return jsonObject;
    }
    
    public static GetChannelsRequest fromJSON(JSONObject jsonObject){
        return new GetChannelsRequest();
    }
}
