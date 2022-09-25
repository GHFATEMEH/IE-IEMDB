package Entity;

import Utils.Functions;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class Actor extends Entity{
    private int id;
    private String nationality;

    public Actor(Map<String, Object> values){
        id = ((com.fasterxml.jackson.databind.node.IntNode) values.get("id")).intValue();
        updateInfo(values);
    }

    public void updateInfo(Map<String, Object> values){
        name = ((com.fasterxml.jackson.databind.node.TextNode) values.get("name")).asText();
        birthDate = ((com.fasterxml.jackson.databind.node.TextNode) values.get("birthDate")).asText();
        nationality = ((com.fasterxml.jackson.databind.node.TextNode) values.get("nationality")).asText();
    }

    public int getId(){
        return id;
    }

    public JSONObject getActorInfo() throws JSONException {
        Functions functions = new Functions();
        JSONObject info = functions.createJSONObjectWithOrder();
        info.put("actorId", id);
        info.put("name", name);
        return info;
    }
}
