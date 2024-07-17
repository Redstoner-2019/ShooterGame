package me.redstoner2019.server;

import me.redstoner2019.player.Player;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientHandler {
    private Socket socket;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    private Player player = new Player("Test",0,0,0,0);

    public ClientHandler(Socket socket) {
        this.socket = socket;
        try {
            this.ois = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            this.oos = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Server.players.add(player);

        float deltaTime = (float)(200 / 0.016666666666666666);

        while (!socket.isClosed()) {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            player.update(deltaTime);
            JSONObject updateData = new JSONObject();

            updateData.put("name",player.getName());
            updateData.put("x",player.getX());
            updateData.put("y",player.getY());
            updateData.put("vx", player.getVy());
            updateData.put("vy", player.getVy());

            JSONArray playerData = new JSONArray();

            for(Player p : Server.players){
                JSONObject data = new JSONObject();
                data.put("name",p.getName());
                data.put("x",p.getX());
                data.put("y",p.getY());
                data.put("vx", p.getVy());
                data.put("vy", p.getVy());

                playerData.put(data);
            }

            updateData.put("playerdata",playerData);

            try {
                oos.writeObject(updateData.toString());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        Server.players.remove(player);
    }
}
