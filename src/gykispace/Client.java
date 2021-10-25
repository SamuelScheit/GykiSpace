package gykispace;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Client implements Runnable {
    private static Client instance;
    private static final String IPv4_PATTERN = "\\d+(\\.\\d+){3}";
    private static final int PORT = 8052;
    private static final int SEPARATOR = 0x1d;
    private static final String PREFIX = "GykiSpace";
    private static final int MAX_PACKET_SIZE = 2048;
    private static final int TIMEOUT = 0;
    private static InetAddress broadcastAddress;
    public DatagramSocket socket;
    public String username;
    public Map<String, InetAddress> rooms = new HashMap();

    public Client() {
        this("Anonymous");
    }

    public Client(String username) {
        this.username = username;

        Client.instance = this;

        try {
            Client.broadcastAddress = InetAddress.getByName("255.255.255.255");
        } catch (Exception e) {
            e.printStackTrace();
        }

        rooms.put("Public", broadcastAddress);
        GUI.getInstance().updateRooms();


        new Thread(this).start();
    }

    public static Client getInstance() {
        if (instance == null) instance = new Client();
        return instance;
    }

    public void send(String value) {
        this.send(username + SEPARATOR + value, broadcastAddress);
    }

    public void send(String value, InetAddress address) {
        String data = PREFIX + value;
        DatagramPacket dp = new DatagramPacket(data.getBytes(), data.length(), address, PORT);

        try {
            socket.send(dp);
        } catch (SocketTimeoutException ste) {
            // time-out while waiting for reply.  Send the broadcast again.
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        try {
            socket = new DatagramSocket(Client.PORT);
            socket.setBroadcast(true);
            socket.setSoTimeout(TIMEOUT);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("You can't start GykiChat multiple times");
            System.exit(-1);
        }

        GUI.getInstance().log("client and server ready");
        send("is now online.");

        while (!socket.isClosed()) {
            try {
                byte[] receiveBuffer = new byte[MAX_PACKET_SIZE];
                DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
                socket.receive(receivePacket);

                String data = new String(receivePacket.getData());

                if (data.startsWith(PREFIX)) {
                    data = data.substring(PREFIX.length());
                    String[] value = data.split(String.valueOf(SEPARATOR));
                    if (value.length != 2) continue;

                    String username = value[0];
                    String content = value[1];

                    rooms.put(username, receivePacket.getAddress());
                    GUI.getInstance().updateRooms();

                    GUI.getInstance().log(username + ": " + content);
                }
            } catch (Exception e) {
                socket.close();
                run();
            }
        }

    }
}
