import java.io.Serializable;
import java.util.Date;

import org.json.simple.JSONObject;

public class Message implements Serializable {
    private static final String _class = Request.class.getSimpleName();
    private final String from;
    private final long when;
    private final String body;

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

    public static Message fromJSON(Object object){
        JSONObject jsonObject = (JSONObject) object;
        String from = (String) jsonObject.get("from");
        // TODO: 21/12/2021 MAKE SURE THIS IS CLEANED BEFORE SENDING 
//        long when = (long) jsonObject.get("when");
//        Server Side set the when to when the message was recieved.
        long when = System.currentTimeMillis() / 1000L;
        String body = (String) jsonObject.get("body");
        return new Message(from, when, body);
    }
}