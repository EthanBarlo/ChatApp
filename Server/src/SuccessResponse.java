import org.json.simple.JSONObject;

public class SuccessResponse extends Response {
    private static final String _class = SuccessResponse.class.getSimpleName();

    @SuppressWarnings("unchecked")
    public Object toJSON(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("_class", _class);
        return jsonObject;
    }

    public static SuccessResponse fromJSON(Object object){
        try{
            JSONObject jsonObject = (JSONObject) object;
            if(jsonObject.get("_class").equals("SuccessResponse"))
                return new SuccessResponse();
            return null;
        } catch (ClassCastException | NullPointerException e){
            return null;
        }
    }
}
