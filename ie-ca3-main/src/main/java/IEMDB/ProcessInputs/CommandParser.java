package IEMDB.ProcessInputs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javafx.util.Pair;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CommandParser {

    private Map<String, Object> parseJson(String json) throws JsonProcessingException {
        JsonFactory factory = new JsonFactory();

        ObjectMapper mapper = new ObjectMapper(factory);
        JsonNode rootNode = mapper.readTree(json);
        Map<String, Object> jsonData = new HashMap<String, Object>();

        Iterator<Map.Entry<String, JsonNode>> fieldsIterator = rootNode.fields();
        while (fieldsIterator.hasNext()) {
            Map.Entry<String, JsonNode> field = fieldsIterator.next();
            jsonData.put(field.getKey(), field.getValue());
        }
        return jsonData;
    }

    public ArrayList<Map<String, Object>> parseStringArray(ArrayList<String> data) throws JsonProcessingException {
        ArrayList<Map<String, Object>> out = new ArrayList<>();
        for(String s : data)
            out.add(parseJson(s));
        return out;
    }
}