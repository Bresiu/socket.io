import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws JSONException {
        QueryItems queryItems = new QueryItems();
        queryItems.makeQueries();

        List<String> players = new ArrayList<String>(2);
        players.add("05f973b9dc022ae0d82adce63676bf64");
        players.add("5ed15ac2800b7140d0dfaa95ecdef82f");

        for (String player : players) {
            Socketio socetio = new Socketio(player);
            socetio.connect();
        }
    }
}
