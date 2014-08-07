import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

public class Socetio {
    String player;

    public Socetio(String player) {
        this.player = player;
    }

    public void connect() {
        final Socket socket;
        IO.Options opts = new IO.Options();
        opts.forceNew = true;
        try {
            socket = IO.socket("http://localhost:3006", opts);
        } catch (URISyntaxException e) {
            throw new IllegalStateException(e);
        }

        socket.on("process", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                System.out.println("on process: " + player);
                String json = (String) args[0];
                System.out.println(json);
            }
        }).on("terminate", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                System.out.println("on terminate: " + player);
                System.out.println(args[0].toString());
            }
        }).on(Socket.EVENT_CONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                System.out.println("on connect: " + player);
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("player_id", player);
                    jsonObject.put("event_type", "initialize");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                System.out.println("emiting: " + jsonObject.toString());
                socket.emit("initialize", jsonObject.toString());
            }
        }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                System.out.println("on disconnect: " + player);
            }
        });
        socket.open();
    }
}