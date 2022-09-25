package ProcessInputs;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javafx.util.Pair;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CommandParser {
    public Pair<String,Map<String, Object>> parse(String line) throws JsonProcessingException {
        Map<String, Object> result = new HashMap<String, Object>();
        String parts[] = line.split(" ", 2);
        if (parts.length != 1)
            result = parseJson(parts[1]);
        return new Pair<String,Map<String, Object>> (parts[0], result);
    }

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
}
