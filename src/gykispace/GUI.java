package gykispace;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class GUI extends JFrame {
    private JButton send;
    private JTextField input;
    private JPanel mainPanel;
    private JTabbedPane tabbar;
    private Map<String, JScrollPane> scroller = new HashMap<>();
    private Map<String, JPanel> lists = new HashMap<>();
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
                    Client.getInstance().send(input.getText());
                    input.setText("");
                    String room = tabbar.getTitleAt(tabbar.getSelectedIndex());

                    scroller.get(room).getVerticalScrollBar().setValue(scroller.get(room).getVerticalScrollBar().getMaximum());
                }
            }

        });

        /*
        theme.setModel(new DefaultComboBoxModel<>(Arrays.stream(UIManager.getInstalledLookAndFeels()).map(UIManager.LookAndFeelInfo::getName).toArray()));

        GUI self = this;
        theme.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                try {
                    for (var lnf : UIManager.getInstalledLookAndFeels()) {
                        if (lnf.getName().equals(theme.getSelectedItem())) {
                            System.out.println("set theme to: "+lnf.getClassName());
                            UIManager.setLookAndFeel(lnf.getClassName());
                            SwingUtilities.updateComponentTreeUI(self);
                            self.pack();
                            return;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println(theme.getSelectedItem());
            }
        });
         */
    }

    public void updateRooms() {
        Client.getInstance().rooms.forEach((name, address) -> {
            if (tabbar.indexOfTab(name) == -1) {
                var list = new JPanel();
                list.setLayout(new BoxLayout(list, BoxLayout.Y_AXIS));

                var container = new JScrollPane(list, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
                container.setLayout(new ScrollPaneLayout());
                scroller.put(name, container);

                lists.put(name, list);
                tabbar.addTab(name, null, container, address.getHostAddress());
            }
        });
    }

    public void log(String name, String value) {
        JLabel label = new JLabel(value);
        if (tabbar.indexOfTab(name) == -1) return;

        lists.get(name).add(label);
        scroller.get(name).validate();
    }

    public void log(String value) {
        if (tabbar.getSelectedIndex() == -1) return;
        this.log(tabbar.getTitleAt(tabbar.getSelectedIndex()), value);
    }

}
