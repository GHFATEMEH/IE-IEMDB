
import ProcessInputs.CommandHandler;
import ProcessInputs.CommandParser;
import ProcessOutputs.OutputGenerator;
import javafx.util.Pair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.lang.System;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import Exception.*;

public class Controller {
    private CommandHandler commandHandler;
    private CommandParser commandParser;
    private OutputGenerator outputGenerator;

    public Controller() {
        commandHandler = new CommandHandler();
        commandParser = new CommandParser();
        outputGenerator = new OutputGenerator();
    }

    public void Run() throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String line;
        while((line = br.readLine()) != null){
            Map<String, Object> msg = new LinkedHashMap<String, Object>();
            try {
                Pair<String, Map<String, Object>> commandData = commandParser.parse(line);
                msg = commandHandler.handle(commandData);
            }
            catch (OurException e) {
                msg.put("success", false);
                msg.put("data", e.getMessage());
            }
            catch (Exception e) {
                msg.put("success", false);
                msg.put("data", "InvalidCommand");
            }
            outputGenerator.generateOutput(msg);
        }
    }
}
