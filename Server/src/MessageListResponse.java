import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.util.ArrayList;
import java.util.List;

public class MessageListResponse extends Response {
    private static final String _class = MessageListResponse.class.getSimpleName();
    private final List<Message> messages;

    public MessageListResponse(List<Message> messages){
        this.messages = messages;
    }

    public List<Message> getMessages() { return messages; }

    @SuppressWarnings("unchecked")
    public Object toJSON(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("_class", _class);
        JSONArray jsonArray = new JSONArray();
        messages.forEach(message -> jsonArray.add(message.toJSONString()));
        jsonObject.put("messages", jsonArray);
        return jsonObject;
    }

    public static MessageListResponse fromJSON(JSONObject jsonObject){
        JSONArray jsonMessages =  (JSONArray) jsonObject.get("messages");
        List<Message> messages = new ArrayList<>();
        jsonMessages.forEach(jsonMessage -> messages.add(Message.fromJSON((JSONObject) JSONValue.parse((String)jsonMessage))));
        return new MessageListResponse(messages);
    }
}