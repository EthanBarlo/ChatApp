import org.json.simple.JSONAware;
import org.json.simple.JSONObject;

import java.util.List;

public abstract class Request implements JSONAware{
    public abstract Object toJSON();
    public String toJSONString() { return toJSON().toString(); }
    public String toString() { return toJSONString(); }
    public abstract Response DoRequest(ClientDetails client);
}


