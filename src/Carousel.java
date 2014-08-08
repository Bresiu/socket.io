import com.github.nkzawa.socketio.client.Socket;

import java.util.Random;

public class Carousel implements Runnable {

    private final Socket socket;
    private final String playerID;
    private final String messageID;
    private String response;

    public Carousel(Socket socket, String playerID,String messageID) {
        this.socket = socket;
        this.playerID = playerID;
        this.messageID = messageID;
        makeResponse();
    }

    private void makeResponse() {
        response = String.format(QueryItems.envelope, playerID, QueryItems.queryItems.get(messageID), messageID);
    }

    private int randInt() {
        Random rand = new Random();
        return rand.nextInt((50 - 10) + 1) + 10;
    }

    @Override
    public void run() {
        System.out.println("emiting");
        socket.emit("process", response);
    }
}
