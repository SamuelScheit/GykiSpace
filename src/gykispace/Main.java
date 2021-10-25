package gykispace;

import javax.swing.*;

public class Main {

    public static void main(String args[]) {
        String username = JOptionPane.showInputDialog("Enter your username: ");
        GUI.getInstance().log("Logged in as " + username);
        new Client(username);
    }
}
