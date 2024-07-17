package me.redstoner2019.server;

import me.redstoner2019.player.Player;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
    public static List<Player> players = new ArrayList<>();
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8010);

        while (serverSocket.isBound()) {
            Socket socket = serverSocket.accept();
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    ClientHandler c = new ClientHandler(socket);
                }
            });
            t.start();
        }
    }
}
