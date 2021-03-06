package com.duoxik.chatbox.client.view.gui;

import com.duoxik.chatbox.client.view.ClientGuiView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainJFrame extends JFrame {

    private final ClientGuiView view;

    private JTextField textField = new JTextField(50);
    private JTextArea messages = new JTextArea(10, 40);
    private JTextArea users = new JTextArea(10, 10);
    private final MenuBar menuBar;

    public MainJFrame(ClientGuiView view) {
        super("ChatBox v1.0");
        this.view = view;
        this.menuBar = new MenuBar(view);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocation(600, 300);
        setResizable(false);
        setJMenuBar(menuBar);

        users.setEditable(false);
        messages.setFont(new Font("Serif", Font.ITALIC, 16));
        messages.setLineWrap(true);
        messages.setWrapStyleWord(true);
        messages.setEditable(false);

        textField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                view.sendTextMessage(textField.getText());
                textField.setText("");
            }
        });

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
        if (clientConnected) {
            menuBar.enableItemDisconnect();
            JOptionPane.showMessageDialog(
                    this,
                    "Соединение с сервером установлено...",
                    "Чат",
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            menuBar.disableItemDisconnect();
            JOptionPane.showMessageDialog(
                    this,
                    "Клиент отключен от сервера...",
                    "Чат",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
