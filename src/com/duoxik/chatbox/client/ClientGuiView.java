package com.duoxik.chatbox.client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ClientGuiView {
    private final ClientGuiController controller;

    private JFrame mainFrame = new JFrame("Чат");
    private JFrame ipPortFrame = new JFrame("ip:port");

    private JTextField textField = new JTextField(50);
    private JTextArea messages = new JTextArea(10, 40);
    private JTextArea users = new JTextArea(10, 10);
    private JButton connectionButton = new JButton("Connect to the server...");

    private String serverAddress;
    private int serverPort;


    public ClientGuiView(ClientGuiController controller) {
        this.controller = controller;
        initMainFrame();
        initIpPortFrame();
    }

    private void initMainFrame() {

        textField.setEditable(false);
        users.setEditable(false);

        messages.setFont(new Font("Serif", Font.ITALIC, 16));
        messages.setLineWrap(true);
        messages.setWrapStyleWord(true);
        messages.setEditable(false);
        mainFrame.setResizable(false);

        connectionButton.setActionCommand("connect");
        connectionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getActionCommand().equals("connect")) {
                    showIpPortFrame();
                } else {
                    controller.abortConnection();
                }
            }
        });

        mainFrame.setLocation(600, 300);
        mainFrame.getContentPane().add(connectionButton, BorderLayout.NORTH);
        mainFrame.getContentPane().add(textField, BorderLayout.SOUTH);
        mainFrame.getContentPane().add(new JScrollPane(messages), BorderLayout.WEST);
        mainFrame.getContentPane().add(new JScrollPane(users), BorderLayout.EAST);
        mainFrame.pack();
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setVisible(true);

        textField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.sendTextMessage(textField.getText());
                textField.setText("");
            }
        });
    }

    private void initIpPortFrame() {

        ipPortFrame.setLocation(800, 400);
        ipPortFrame.setFocusable(false);
        JTextField ipField = new JTextField(10);
        JLabel splitter = new JLabel(":");
        JTextField portField = new JTextField(5);
        JButton buttonOK = new JButton("OK");

        buttonOK.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (validIP(ipField.getText()) &&
                        validPort(portField.getText())) {
                    serverAddress = ipField.getText();
                    serverPort = Integer.parseInt(portField.getText());
                    controller.run();
                    hideIpPortFrame();
                } else {
                    ipPortFrame.setVisible(false);
                    JOptionPane.showMessageDialog(
                        mainFrame,
                        "Был введен некорректный ip или порт сервера. Попробуйте еще раз.",
                        "Конфигурация клиента",
                        JOptionPane.ERROR_MESSAGE);
                    ipPortFrame.setVisible(true);
                }
            }
        });

        ipPortFrame.getContentPane().setLayout(new FlowLayout());
        ipPortFrame.getContentPane().add(ipField);
        ipPortFrame.getContentPane().add(splitter);
        ipPortFrame.getContentPane().add(portField);
        ipPortFrame.getContentPane().add(buttonOK);
        ipPortFrame.pack();
        ipPortFrame.setResizable(false);
        ipPortFrame.setAlwaysOnTop(true);
    }

    private void showIpPortFrame() {

        mainFrame.setEnabled(false);
        ipPortFrame.setVisible(true);
    }

    private void hideIpPortFrame() {

        mainFrame.setEnabled(true);
        ipPortFrame.setVisible(false);
    }

    public String getServerAddress() {
        return serverAddress;
    }

    public int getServerPort() {
        return serverPort;
    }

    public String getUserName() {
        return JOptionPane.showInputDialog(
                mainFrame,
                "Введите ваше имя:",
                "Конфигурация клиента",
                JOptionPane.QUESTION_MESSAGE);
    }

    public void notifyConnectionStatusChanged(boolean clientConnected) {
        textField.setEditable(clientConnected);
        if (clientConnected) {
            JOptionPane.showMessageDialog(
                    mainFrame,
                    "Соединение с сервером установлено...",
                    "Чат",
                    JOptionPane.INFORMATION_MESSAGE);
            connectionButton.setText("Disconnect from the server...");
            connectionButton.setActionCommand("disconnect");
        } else {
            JOptionPane.showMessageDialog(
                    mainFrame,
                    "Клиент отключен от сервера...",
                    "Чат",
                    JOptionPane.INFORMATION_MESSAGE);

            connectionButton.setText("Connect to the server...");
            connectionButton.setActionCommand("connect");
        }

    }

    public void refreshMessages() {
        messages.append(controller.getModel().getNewMessage() + "\n");
    }

    public void refreshUsers() {
        ClientGuiModel model = controller.getModel();
        StringBuilder sb = new StringBuilder();
        for (String userName : model.getAllUserNames()) {
            sb.append(userName).append("\n");
        }
        users.setText(sb.toString());
    }

    public void clearFrame() {
        messages.setText("");
        users.setText("");
        textField.setText("");
    }

    private static boolean validPort(String port) {

        try {

            int portInt = Integer.parseInt(port);
            return (portInt >= 0 && portInt <= 65536) ? true : false;

        } catch (NumberFormatException e) {
            return false;
        }
    }

    private static boolean validIP(String ip) {
        try {
            if ( ip == null || ip.isEmpty() ) {
                return false;
            }

            String[] parts = ip.split( "\\." );
            if ( parts.length != 4 ) {
                return false;
            }

            for ( String s : parts ) {
                int i = Integer.parseInt( s );
                if ( (i < 0) || (i > 255) ) {
                    return false;
                }
            }
            if ( ip.endsWith(".") ) {
                return false;
            }

            return true;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }
}
