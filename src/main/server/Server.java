package main.server;

import java.net.*;
import java.util.LinkedList;
import java.util.List;

public class Server {
    public static int port = 8051;
    public static List<Connection> clients = new LinkedList<>();

    public static void main(String[] args) {
        try {
            ServerSocket server = new ServerSocket(Server.port);
            server.setReuseAddress(true);
            System.out.println("Server ready and waiting for connections");

            while (true) {
                Socket client = server.accept();

                System.out.println("New client connected " + client.getInetAddress().getHostAddress() + ":" + client.getPort());

                Connection connection = new Connection(client);
                Server.clients.add(connection);

                new Thread(connection).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
