import org.json.simple.JSONObject;

public class SubscribeRequest extends Request{
    private static final String _class = SubscribeRequest.class.getSimpleName();
    private final String identity; // Client
    private final String channel;

    public SubscribeRequest(String identity, String channel){
        this.identity = identity;
        this.channel = channel;
    }

    public String getIdentity() { return identity; }
    public String getChannel() {return channel;}

    @SuppressWarnings("unchecked")
    public Object toJSON(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("_class", _class);
        jsonObject.put("identity", identity);
        jsonObject.put("channel", channel);
        return jsonObject;
    }

    public static SubscribeRequest fromJSON(JSONObject jsonObject){
        String identity = (String) jsonObject.get("identity");
        String channel = (String) jsonObject.get("channel");
        return new SubscribeRequest(identity, channel);
    }

    public Response DoRequest(ClientDetails client){
        String errorMessage = client.Subscribe(channel);
        if(errorMessage != null)
            return new ErrorResponse(errorMessage);
        
        return new SuccessResponse();
    }
}
