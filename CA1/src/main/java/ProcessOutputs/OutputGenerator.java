package ProcessOutputs;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;

import Utils.Functions;
import org.json.JSONException;
import org.json.JSONObject;

public class OutputGenerator {
    public void generateOutput(Map<String, Object> msg) throws JSONException {
        Functions functions = new Functions();
        JSONObject jsonObject = functions.createJSONObjectWithOrder();
        for(String key : msg.keySet())
            jsonObject.put(key, msg.get(key));
        System.out.println(jsonObject.toString());
    }
}




