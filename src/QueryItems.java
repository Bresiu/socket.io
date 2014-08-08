import java.util.HashMap;

public class QueryItems {
    public static HashMap<String, String> queryItems = new HashMap<String, String>();
    public static String envelope = "{\"player_id\":\"%s\",\"event_type\":\"process\",\"message\":{\"request\":null," +
            "\"response\":{%s},\"id\":\"%s\"}}";

    public void makeQueries() {
        queryItems.put("G7H8J9", "\"value_collection\": [20, 16, 54]");
        queryItems.put("D4E5F6", "\"value\": 1");
        queryItems.put("A1B2C3", "\"value_a\": 27, \"value_b\": 41");
   }
}