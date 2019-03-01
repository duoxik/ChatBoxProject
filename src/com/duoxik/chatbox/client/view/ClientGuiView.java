package com.duoxik.chatbox.client.view;

import com.duoxik.chatbox.client.ClientGuiController;
import com.duoxik.chatbox.client.ClientGuiModel;

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

    ClientGuiController getController() {
        return controller;
    }

    void showIpPortFrame() {
        connectJFrame.setVisible(true);
    }

    void hideIpPortFrame() {

        mainJFrame.setEnabled(true);
        connectJFrame.setVisible(false);
    }
}