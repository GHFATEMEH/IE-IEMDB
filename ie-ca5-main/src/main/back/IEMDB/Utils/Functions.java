package IEMDB.Utils;

import com.fasterxml.jackson.databind.node.ArrayNode;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class Functions {

    public ArrayList<String> convertToListString(Object valuesObj) {
        ArrayNode values = (com.fasterxml.jackson.databind.node.ArrayNode)valuesObj;
        final ArrayList<String> result = new ArrayList<>(values.size());
        values.forEach(jsonNode -> result.add(jsonNode.asText()));
        return result;
    }

    public ArrayList<Integer> convertToListInt(Object valuesObj) {
        ArrayNode values = (com.fasterxml.jackson.databind.node.ArrayNode)valuesObj;
        final ArrayList<Integer> result = new ArrayList<>(values.size());
        values.forEach(jsonNode -> result.add(jsonNode.intValue()));
        return result;
    }

    public JSONObject createJSONObjectWithOrder(){
        JSONObject jsonObject = new JSONObject();
        try {
            Field changeMap = jsonObject.getClass().getDeclaredField("map");
            changeMap.setAccessible(true);
            changeMap.set(jsonObject, new LinkedHashMap<>());
            changeMap.setAccessible(false);
        } catch (IllegalAccessException | NoSuchFieldException e) { }
        return jsonObject;
    }
}


