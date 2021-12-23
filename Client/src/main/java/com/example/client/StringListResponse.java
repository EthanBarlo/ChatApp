package com.example.client;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class StringListResponse extends Response {
    private static final String _class = StringListResponse.class.getSimpleName();
    private final List<String> data;

    public StringListResponse(List<String> data){
        this.data = data;
    }

    public List<String> getData() { return data; }

    @SuppressWarnings("unchecked")
    public Object toJSON(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("_class", _class);
        JSONArray jsonArray = new JSONArray();
        jsonArray.addAll(data);
        jsonObject.put("data", jsonArray);
        return jsonObject;
    }

    public static StringListResponse fromJSON(JSONObject jsonObject){
        JSONArray jsonArray =  (JSONArray) jsonObject.get("data");
        List<String> data = new ArrayList<>(jsonArray);
        return new StringListResponse(data);
    }
}