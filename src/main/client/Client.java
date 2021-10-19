package main.client;

import java.net.*;
import java.io.*;


public class Client implements Runnable {
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    private String ip;
    private int port;

    public Client(String ip, int port) throws Exception {
        this.ip = ip;
        this.port = port;
    }

    public static void main(String[] args) {
        try {
            Client client = new Client("localhost", 8051);
            new Thread(client).start();
            client.handleInput();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void handleInput() throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        while (true) {
            String input = reader.readLine();

            if (input.contains("stop") || input.contains("exit")) {
                this.stopConnection();
                return;
            }

            this.out.println(input);
            this.out.flush();
        }
    }

    public void run() {
        try {
            clientSocket = new Socket(ip, port);
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            System.out.println("connected");
            String line;
            while ((line = in.readLine()) != null) {
                System.out.println(line);
            }
        } catch (Exception e) {
            System.out.println("disconnected");
        }
    }

    public void stopConnection() throws Exception {
        in.close();
        out.close();
        clientSocket.close();
        System.exit(0);
    }
}
