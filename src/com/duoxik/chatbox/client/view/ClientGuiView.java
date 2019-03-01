package com.duoxik.chatbox.client.view;

import com.duoxik.chatbox.client.ClientGuiController;
import com.duoxik.chatbox.client.ClientGuiModel;
import com.duoxik.chatbox.client.view.gui.ConnectJFrame;
import com.duoxik.chatbox.client.view.gui.MainJFrame;

public class ClientGuiView {
    private final ClientGuiController controller;

    private MainJFrame mainJFrame = new MainJFrame(this);
    private ConnectJFrame connectJFrame = new ConnectJFrame(this);

    public ClientGuiView(ClientGuiController controller) {
        this.controller = controller;
    }

    public String getServerAddress() {
        return connectJFrame.getServerAddress();
    }

    public int getServerPort() {
        return connectJFrame.getServerPort();
    }

    public String getUserName() {
        return connectJFrame.getUserName();
    }

    public void notifyConnectionStatusChanged(boolean clientConnected) {
        mainJFrame.notifyConnectionStatusChanged(clientConnected);
    }

    public void refreshMessages() {
        mainJFrame.appendMessage(controller.getModel().getNewMessage());
    }

    public void refreshUsers() {
        ClientGuiModel model = controller.getModel();
        StringBuilder sb = new StringBuilder();
        for (String userName : model.getAllUserNames()) {
            sb.append(userName).append("\n");
        }
        mainJFrame.setUsers(sb.toString());
    }

    public void clearFrame() {
        mainJFrame.clearFrame();
    }

    public void showConnectFrame() {
        connectJFrame.setVisible(true);
    }

    public void connectToServer() {
        controller.run();
    }

    public void abortConnection() {
        controller.abortConnection();
    }

    public void sendTextMessage(String text) {
        controller.sendTextMessage(text);
    }
}
