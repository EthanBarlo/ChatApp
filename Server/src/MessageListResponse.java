import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MessageListResponse extends Response {
    private static final String _class = Request.class.getSimpleName();
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

    public static MessageListResponse fromJSON(Object object){
        try{
            JSONObject jsonObject = (JSONObject) object;
            JSONArray jsonMessages =  (JSONArray) jsonObject.get("messages");
            List<Message> messages = new ArrayList<>();
            jsonMessages.forEach(jsonMessage -> messages.add(Message.fromJSON(jsonMessage)));
            return new MessageListResponse(messages);
        } catch (ClassCastException | NullPointerException e){
            return null;
        }
    }
}