import org.json.simple.JSONObject;

import java.util.List;

public class GetChannelsRequest extends Request {
    private static final String _class = GetChannelsRequest.class.getSimpleName();

    @SuppressWarnings("unchecked")
    public Object toJSON() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("_class", _class);
        return jsonObject;
    }

    public static GetChannelsRequest fromJSON(JSONObject jsonObject) {
        return new GetChannelsRequest();
    }

    public Response DoRequest(ClientDetails client) {
        List<String> channelNames = client.getServer().getChannelNames();
        if (channelNames == null) return new ErrorResponse("GetChannelsRequest Failed!");
        return new StringListResponse(channelNames);
    }
}
