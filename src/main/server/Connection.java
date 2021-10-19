package main.server;

import java.net.*;
import java.io.*;

public class Connection implements Runnable {
    public final Socket clientSocket;
    PrintWriter out = null;
    BufferedReader in = null;

    public Connection(Socket socket) {
        this.clientSocket = socket;
    }

    public void run() {
        try {
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            String line;
            while ((line = in.readLine()) != null) {
                System.out.printf("Sent from the client: %s\n", line);
                for (var client : Server.clients) {
                    if (client == this) continue;
                    client.out.println(line);
                }
            }
        } catch (Exception e) {
        }

        try {
            if (out != null) {
                out.close();
            }
            if (in != null) {
                in.close();
                clientSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Server.clients.remove(this);
    }
}
