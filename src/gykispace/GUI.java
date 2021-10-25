package gykispace;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class GUI extends JFrame {
    private JButton send;
    private JTextField input;
    private JPanel mainPanel;
    private JTextPane text;
    private JScrollPane scrollPanel;
    private JPanel messages;
    private JTabbedPane tabs;
    private static GUI instance;

    static public GUI getInstance() {
        if (instance == null) instance = new GUI();
        return instance;
    }

    private GUI() {
        setContentPane(mainPanel);
        setTitle("GykiSpace");
        setSize(800, 600);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);

        send.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        input.grabFocus();

        input.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    // Send
                    Client.getInstance().send(Client.getInstance().username + ": " + input.getText());
                    input.setText("");
                    scrollPanel.getVerticalScrollBar().setValue(scrollPanel.getVerticalScrollBar().getMaximum());
                }
            }

        });
        messages.setLayout(new BoxLayout(messages, BoxLayout.Y_AXIS));
        scrollPanel.setMaximumSize(new Dimension(200, 200));
    }

    public void log(String value) {
        JLabel label = new JLabel(value);
        messages.add(label);
        scrollPanel.validate();
    }


}
