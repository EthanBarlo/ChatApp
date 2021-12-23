package com.example.client;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javafx.scene.layout.Pane;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class Message implements Serializable {
        private static final String _class = com.example.client.Message.class.getSimpleName();
        private final String from; // Who sent the message
        private final long when;
        private final String body; // Message text
        private final File file;

        public Message(String from, long when, String body, File file){
            this.from = from;
            this.when = when;
            this.body = body;
            this.file = file;
        }
        public Message(String from, long when, String body){
            this.from = from;
            this.when = when;
            this.body = body;
            this.file = null;
        }

        public String getFrom(){ return from; }
        public String getBody() { return body; }
        public long getWhen() {return when;}
        public File getFile() {return file;}


    @SuppressWarnings("unchecked")
        public String toJSONString(){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("_class", _class);
            jsonObject.put("from", from);
            jsonObject.put("when", when);
            jsonObject.put("body", body);
            if(file != null){
                JSONArray jsonArray = new JSONArray();
                try{
                    byte[] byteArray = Files.readAllBytes(file.toPath());
                    for (byte b : byteArray) {
                        jsonArray.add(b);
                    }
                }catch (IOException ie){
                    ie.printStackTrace();
                }
                jsonObject.put("fileBytes", jsonArray);
            }
            return jsonObject.toJSONString();
        }

        public static Message fromJSON(JSONObject jsonObject){
            String from = (String) jsonObject.get("from");
            long when = (long) jsonObject.get("when");
            String body = (String) jsonObject.get("body");
            if(jsonObject.get("fileBytes") == null)
                return new com.example.client.Message(from, when, body);

            JSONArray fileBytes = (JSONArray)jsonObject.get("fileBytes");
            byte[] byteArray = new byte[fileBytes.size()];
            for (int i = 0; i < fileBytes.size(); i++) {
                byteArray[i] = (byte) ((long)fileBytes.get(i));
            }

            try{
                new File("clientData/").mkdirs();
                File file = new File("clientData/"+body);
                file.createNewFile();
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                fileOutputStream.write(byteArray);
                fileOutputStream.close();

            }catch (IOException ie){
                ie.printStackTrace();
            }

            File file = new File("clientData/"+body);
            return new Message(from, when, body, file);
        }
}
