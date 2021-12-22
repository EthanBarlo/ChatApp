import org.json.simple.JSONObject;

public class UnsubscribeRequest extends Request{
    private static final String _class = UnsubscribeRequest.class.getSimpleName();
    private final String identity;
    private final String channel;

    public UnsubscribeRequest(String identity, String channel){
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

    public static UnsubscribeRequest fromJSON(JSONObject jsonObject){
        String identity = (String) jsonObject.get("identity");
        String channel = (String) jsonObject.get("channel");
        return new UnsubscribeRequest(identity, channel);
    }

    public Response DoRequest(ClientDetails client){
        String errorMessage = client.Unsubscribe(channel);
        if(errorMessage != null)
            return new ErrorResponse(errorMessage);

        return new SuccessResponse();
    }
}