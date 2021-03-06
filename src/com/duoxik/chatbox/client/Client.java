package com.duoxik.chatbox.client;

import com.duoxik.chatbox.Connection;
import com.duoxik.chatbox.ConsoleHelper;
import com.duoxik.chatbox.Message;
import com.duoxik.chatbox.MessageType;

import java.io.IOException;
import java.net.Socket;

public class Client {

    protected Connection connection;
    protected volatile boolean clientConnected = false;

    protected String getServerAddress() {
        return ConsoleHelper.readString();
    }

    protected int getServerPort() {
        return ConsoleHelper.readInt();
    }

    protected String getUserName() {
        return ConsoleHelper.readString();
    }

    protected boolean shouldSendTextFromConsole() {
        return true;
    }

    protected SocketThread getSocketThread() {
        return new SocketThread();
    }

    public void sendTextMessage(String text) {

        try {
            Message message = new Message(MessageType.TEXT, text);
            connection.send(message);
        } catch (IOException e) {
            ConsoleHelper.writeMessage(e.getMessage());
            clientConnected = false;
        }
    }

    protected void abortConnection() {
        try {
            connection.close();
        } catch (IOException e) {}
    }

    protected void run() {

        SocketThread socketThread = getSocketThread();
        socketThread.setDaemon(true);
        socketThread.start();

        synchronized (this) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                ConsoleHelper.writeMessage(e.getMessage());
            }
        }

        if (clientConnected) {
            ConsoleHelper.writeMessage("Соединение установлено. " +
                    "Для выхода наберите команду 'exit'.");

            String text;
            while (clientConnected &&
                    !(text = ConsoleHelper.readString()).equals("exit")) {

                if (shouldSendTextFromConsole())
                    sendTextMessage(text);
            }
        } else {
            ConsoleHelper.writeMessage("Произошла ошибка во время работы клиента.");
        }
    }

    public class SocketThread extends Thread {

        protected void processIncomingMessage(String message) {
            ConsoleHelper.writeMessage(message);
        }

        protected void informAboutAddingNewUser(String userName) {
            ConsoleHelper.writeMessage(
                    String.format("%s присоединился к чату.", userName)
            );
        }

        protected void informAboutDeletingNewUser(String userName) {
            ConsoleHelper.writeMessage(
                    String.format("%s покинул чат.", userName)
            );
        }

        protected void notifyConnectionStatusChanged(boolean clientConnected) {
            Client.this.clientConnected = clientConnected;
            synchronized (Client.this) {
                Client.this.notify();
            }
        }

        protected void clientHandshake() throws IOException, ClassNotFoundException {

            while (true) {

                Message message = connection.receive();

                if (message.getType() == MessageType.NAME_REQUEST) {
                    String userName = getUserName();
                    connection.send(new Message(MessageType.USER_NAME, userName));
                } else if (message.getType() == MessageType.NAME_ACCEPTED) {
                    notifyConnectionStatusChanged(true);
                    break;
                } else {
                    throw new IOException("Unexpected MessageType");
                }
            }
        }

        protected void clientMainLoop() throws IOException, ClassNotFoundException {

            while (true) {

                Message message = connection.receive();

                if (message.getType() == MessageType.TEXT) {
                    processIncomingMessage(message.getData());
                } else if (message.getType() == MessageType.USER_ADDED) {
                    informAboutAddingNewUser(message.getData());
                } else if (message.getType() == MessageType.USER_REMOVED) {
                    informAboutDeletingNewUser(message.getData());
                } else {
                    throw new IOException("Unexpected MessageType");
                }
            }
        }

        public void run() {

            String serverAddress = getServerAddress();
            int serverPort = getServerPort();

            try {
                connection = new Connection(new Socket(serverAddress, serverPort));
                clientHandshake();
                clientMainLoop();
            } catch (IOException | ClassNotFoundException e) {
                notifyConnectionStatusChanged(false);
                ConsoleHelper.writeMessage(e.getMessage());
            } finally {
                try {
                    connection.close();
                } catch (IOException e) {
                    ConsoleHelper.writeMessage(e.getMessage());
                }
            }
        }
    }

    public static void main(String[] args) {
        new Client().run();
    }
}
