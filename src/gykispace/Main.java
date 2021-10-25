package gykispace;

import javax.swing.*;

public class Main {

    public static void main(String args[]) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
        }

        String username = "";
        while (username.length() <= 0) {
            username = JOptionPane.showInputDialog("Enter your username: ").trim();
        }

        new Client(username);
    }
}
