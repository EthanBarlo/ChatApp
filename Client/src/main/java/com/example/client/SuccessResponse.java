package com.example.client;

import org.json.simple.JSONObject;

public class SuccessResponse extends Response {
    private static final String _class = SuccessResponse.class.getSimpleName();

    @SuppressWarnings("unchecked")
    public Object toJSON(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("_class", _class);
        return jsonObject;
    }

    public static SuccessResponse fromJSON(JSONObject jsonObject){
        return new SuccessResponse();
    }
}
