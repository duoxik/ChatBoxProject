package com.duoxik.chatbox.client.frames;

import com.duoxik.chatbox.client.ClientGuiView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ConnectJFrame extends JFrame {

    private String serverAddress;
    private int serverPort;
    private String userName;

    private final ClientGuiView view;

    public ConnectJFrame(ClientGuiView view) {
        super("Connect");
        this.view = view;

        setSize(375, 185);
        setResizable(false);
        setAlwaysOnTop(true);
        setLocationRelativeTo(null);

        JButton connectButton = new JButton("Connect");
        JButton cancelButton = new JButton("Cancel");
        connectButton.setSize(50, 25);
        connectButton.setFont(new Font(Font.SANS_SERIF, 0, 11));
        cancelButton.setSize(90, 25);
        cancelButton.setFont(new Font(Font.SANS_SERIF, 0, 11));

        JTextField ipField = new JTextField(21);
        JTextField portField = new JTextField(10);
        JTextField userNameField = new JTextField(32);
        ipField.setText("localhost");
        portField.setText("27015");
        userNameField.setText("ChatBoxUser");

        JLabel addressLabel = new JLabel("Server Nickname or Address:");
        JLabel portLabel = new JLabel("Server port:");
        JLabel userNameLabel = new JLabel("Nickname:");

        addressLabel.setFont(new Font(Font.SANS_SERIF, Font.ITALIC, 12));
        portLabel.setFont(new Font(Font.SANS_SERIF, Font.ITALIC, 12));
        userNameLabel.setFont(new Font(Font.SANS_SERIF, Font.ITALIC, 12));

        final JPanel contentCenter = new JPanel(new GridBagLayout());
        contentCenter.add(addressLabel, new GridBagConstraints(0, 0, 1, 1, 0, 0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, new Insets(12, 10, 3, 0), 0, 0));
        contentCenter.add(portLabel, new GridBagConstraints(1, 0, 1, 1, 0, 0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, new Insets(12, 6, 3, 0), 0, 0));
        contentCenter.add(ipField, new GridBagConstraints(0, 1, 1, 1, 0, 0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, new Insets(3, 10, 3, 0), 0, 0));
        contentCenter.add(portField, new GridBagConstraints(1, 1, 1, 1, 0, 0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, new Insets(3, 6, 3, 10), 0, 0));
        contentCenter.add(userNameLabel, new GridBagConstraints(0, 2, 1, 1, 0, 0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, new Insets(3, 10, 3, 0), 0, 0));
        contentCenter.add(userNameField, new GridBagConstraints(0, 3, 2, 1, 0, 0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, new Insets(3, 10, 5, 0), 0, 0));
        contentCenter.add(connectButton, new GridBagConstraints(0, 4, 2, 1, 1, 0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, new Insets(5, 205, 12, 0), 0, 0));
        contentCenter.add(cancelButton, new GridBagConstraints(1, 4, 1, 1, 0, 0, GridBagConstraints.EAST,
                GridBagConstraints.NONE, new Insets(5, 0, 12, 10), 0, 0));

        getContentPane().add(contentCenter, BorderLayout.CENTER);
        contentCenter.setBackground(new Color(239, 235, 231));
        pack();

        connectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (validIP(ipField.getText()) &&
                        validPort(portField.getText()) &&
                        validUserName(userNameField.getText())) {
                    serverAddress = ipField.getText();
                    serverPort = Integer.parseInt(portField.getText());
                    userName = userNameField.getText();
                    view.getController().run();
                    setVisible(false);
                } else {
                    setVisible(false);
                    JOptionPane.showMessageDialog(
                        null,
                        "Был введен некорректный ip, порт сервера или имя пользователя. Попробуйте еще раз.",
                        "Конфигурация клиента",
                        JOptionPane.ERROR_MESSAGE);
                    setVisible(true);
                }
            }
        });
    }

    public String getServerAddress() {
        return serverAddress;
    }

    public int getServerPort() {
        return serverPort;
    }

    public String getUserName() {
        return userName;
    }

    private static boolean validUserName(String userName) {
        return (userName != null && !userName.isEmpty()) ? true : false;
    }

    private static boolean validPort(String port) {

        try {
            int portInt = Integer.parseInt(port);
            return (portInt >= 0 && portInt <= 65536) ? true : false;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private static boolean validIP(String serverAddress) {
        return (serverAddress != null && !serverAddress.isEmpty()) ? true : false;
    }
}
