import org.json.simple.JSONObject;

public class CreateChannelRequest extends  Request{
    private static final String _class = CreateChannelRequest.class.getSimpleName();
    private String name;

    public CreateChannelRequest(String name){ this.name = name; }

    public String getName() {return name;}

    @SuppressWarnings("unchecked")
    public Object toJSON(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("_class", _class);
        jsonObject.put("name", name);
        return jsonObject;
    }

    public static CreateChannelRequest fromJSON(JSONObject jsonObject){
        String name = (String) jsonObject.get("name");
        return new CreateChannelRequest(name);
    }

    public Response DoRequest(ClientDetails client){
        Server server = client.getServer();
        server.createChannel(name);
        if(server.getChannel(name) == null)
            return new ErrorResponse("Channel could not be created.");
        client.Subscribe(name);
        return new SuccessResponse();
    }
}
