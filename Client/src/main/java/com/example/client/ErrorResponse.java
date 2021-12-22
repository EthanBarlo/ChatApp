package com.example.client;

import org.json.simple.JSONObject;

public class ErrorResponse extends Response {
    private static final String _class = ErrorResponse.class.getSimpleName();
    private final String error;

    public ErrorResponse(String error){
        this.error = error;
    }

    public String getError() { return error; }

    @SuppressWarnings("unchecked")
    public Object toJSON(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("_class", _class);
        jsonObject.put("error", error);
        return jsonObject;
    }

    public static ErrorResponse fromJSON(JSONObject jsonObject){
        String error = (String) jsonObject.get("error");
        return new ErrorResponse(error);
    }
}