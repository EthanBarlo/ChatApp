import org.json.simple.JSONObject;

import java.util.List;

public class GetChannelsRequest extends  Request{
    private static final String _class = GetChannelsRequest.class.getSimpleName();
    private String type;

    public GetChannelsRequest(String type){ this.type = type; }
    @SuppressWarnings("unchecked")
    public Object toJSON(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("_class", _class);
        jsonObject.put("type", type);
        return jsonObject;
    }

    public static GetChannelsRequest fromJSON(JSONObject jsonObject){
        String type = (String) jsonObject.get("type");
        return new GetChannelsRequest(type);
    }
    public Response DoRequest(ClientDetails client) {
        List<String> channelNames;
        if(type.equals("all"))
            channelNames = client.getServer().getChannelNames();
        else
            channelNames = client.getSubscribedChannelNames();

        if (channelNames == null) return new ErrorResponse("GetChannelsRequest Failed!");
        return new StringListResponse(channelNames);
    }
}
