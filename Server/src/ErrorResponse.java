import org.json.simple.JSONObject;

public class ErrorResponse extends Response {
    private static final String _class = Request.class.getSimpleName();
    private final String message;

    public ErrorResponse(String message){
        this.message = message;
    }

    public String getMessage() { return message; }

    @SuppressWarnings("unchecked")
    public Object toJSON(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("_class", _class);
        jsonObject.put("message", message);
        return jsonObject;
    }

    public static ErrorResponse fromJSON(Object object){
        try{
            JSONObject jsonObject = (JSONObject) object;
            String message = (String) jsonObject.get("message");
            return new ErrorResponse(message);
        } catch (ClassCastException | NullPointerException e){
            return null;
        }
    }
}