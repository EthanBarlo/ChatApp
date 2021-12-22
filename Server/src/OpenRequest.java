import org.json.simple.JSONObject;

public class OpenRequest extends Request{
    private static final String _class = OpenRequest.class.getSimpleName();
    private final String username;
    private final String password;

    public OpenRequest(String username, String password){
        this.username = username;
        this.password = password;
    }

    public String getUsername() { return username; }
    public String getPassword() { return password; }

    @SuppressWarnings("unchecked")
    public Object toJSON(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("_class", _class);
        jsonObject.put("username", username);
        jsonObject.put("password", password);
        return jsonObject;
    }

    public static OpenRequest fromJSON(JSONObject jsonObject){
        String username = (String) jsonObject.get("username");
        String password = (String) jsonObject.get("password");
        return new OpenRequest(username, password);
    }

    public Response DoRequest(ClientDetails client){
        // DoRequest is only called from ClientThread, which is only instantiated when a user has logged in.
        return new ErrorResponse("An OpenRequest was sent while client is already signed in!");
    }
}
