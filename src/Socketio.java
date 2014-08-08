import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class Socketio {
    private String player;
    private Carousel carousel;

    public Socketio(String player) {
        this.player = player;
    }

    public void connect() {
        ScheduledExecutorService spinner = Executors.newScheduledThreadPool(2);
        Map<String, ScheduledFuture<?>> futures = new HashMap<>();

        final Socket socket;
        IO.Options opts = new IO.Options();
        opts.forceNew = true;

        try {
            socket = IO.socket("http://localhost:3006", opts);
        } catch (URISyntaxException e) {
            throw new IllegalStateException(e);
        }

        socket.on("process", args -> {
            System.out.println("on process: " + player);
            String messageID = null;
            String playerID = null;

            try {
                JSONObject jsonObject = new JSONObject((String) args[0]);
                System.out.println("Received message: " + jsonObject.toString());
                playerID = jsonObject.getString("player_id");
                messageID = jsonObject.getJSONObject("message").getString("id");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            carousel = new Carousel(socket, playerID, messageID);
            ScheduledFuture<?> future = spinner.scheduleAtFixedRate(carousel, 0, 3, TimeUnit.SECONDS);
            futures.put(messageID, future);
        }).on("terminate", args -> {
            System.out.println("on terminate: " + player);
            futures.values().stream().forEach(f -> f.cancel(true));
            futures.clear();
            System.out.println("terminating scheduled: " + player);
        }).on(Socket.EVENT_CONNECT, args -> {
            System.out.println("on connect: " + player);
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("player_id", player);
                jsonObject.put("event_type", "initialize");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            socket.emit("initialize", jsonObject.toString());
        }).on(Socket.EVENT_DISCONNECT, args -> System.out.println("on disconnect: " + player));
        socket.open();
    }
}