package com.duoxik.chatbox.client.view;

import com.duoxik.chatbox.client.ClientGuiView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainJFrame extends JFrame {

    private final ClientGuiView view;

    private JTextField textField = new JTextField(50);
    private JTextArea messages = new JTextArea(10, 40);
    private JTextArea users = new JTextArea(10, 10);
    private JButton connectionButton = new JButton("Connect to the server...");

    public MainJFrame(ClientGuiView view) {
        super("ChatBox v1.0");
        this.view = view;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocation(600, 300);
        setResizable(false);

        textField.setEditable(false);
        users.setEditable(false);
        messages.setFont(new Font("Serif", Font.ITALIC, 16));
        messages.setLineWrap(true);
        messages.setWrapStyleWord(true);
        messages.setEditable(false);

        connectionButton.setActionCommand("connect");
        connectionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getActionCommand().equals("connect")) {
                    view.showIpPortFrame();
                } else {
                    view.getController().abortConnection();
                }
            }
        });

        textField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                view.getController().sendTextMessage(textField.getText());
                textField.setText("");
            }
        });

        getContentPane().add(connectionButton, BorderLayout.NORTH);
        getContentPane().add(textField, BorderLayout.SOUTH);
        getContentPane().add(new JScrollPane(messages), BorderLayout.WEST);
        getContentPane().add(new JScrollPane(users), BorderLayout.EAST);
        pack();
        setVisible(true);
    }

    public void clearFrame() {
        messages.setText("");
        users.setText("");
        textField.setText("");
    }

    public void setUsers(String users){
        this.users.setText(users);
    }

    public void appendMessage(String message) {
        messages.append(message + "\n");
    }

    public void notifyConnectionStatusChanged(boolean clientConnected) {
        textField.setEditable(clientConnected);
        if (clientConnected) {
            JOptionPane.showMessageDialog(
                    this,
                    "Соединение с сервером установлено...",
                    "Чат",
                    JOptionPane.INFORMATION_MESSAGE);
            connectionButton.setText("Disconnect from the server...");
            connectionButton.setActionCommand("disconnect");
        } else {
            JOptionPane.showMessageDialog(
                    this,
                    "Клиент отключен от сервера...",
                    "Чат",
                    JOptionPane.INFORMATION_MESSAGE);

            connectionButton.setText("Connect to the server...");
            connectionButton.setActionCommand("connect");
        }
    }
}
