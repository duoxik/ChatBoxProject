package com.duoxik.chatbox;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Server {

    private static Map<String, Connection> connectionMap = new ConcurrentHashMap<>();

    public static void main(String[] args) {
        
        int serverPort = ConsoleHelper.readInt();

        try (
                ServerSocket serverSocket = new ServerSocket(serverPort)
        ) {
            ConsoleHelper.writeMessage("Сервер запущен...");

            while (true) {
                Socket socket = serverSocket.accept();
                new Handler(socket).start();
            }

        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public static void sendBroadcastMessage(Message message) {

        for (Connection connection : connectionMap.values()) {
            try {
                connection.send(message);
            } catch (IOException e) {
                ConsoleHelper.writeMessage("Сообщение не было отправлено.");
            }
        }
    }

    private static class Handler extends Thread {

        private Socket socket;

        public Handler(Socket socket) {
            this.socket = socket;
        }

        private String serverHandshake(Connection connection)
                throws IOException, ClassNotFoundException {

            while (true) {
                connection.send(new Message(MessageType.NAME_REQUEST));
                Message userName = connection.receive();

                if (    userName != null &&
                        userName.getType().equals(MessageType.USER_NAME) &&
                        userName.getData() != null &&
                        !userName.getData().isEmpty() &&
                        !connectionMap.containsKey(userName.getData())
                ) {
                    connectionMap.put(userName.getData(), connection);
                    connection.send(new Message(MessageType.NAME_ACCEPTED));
                    return userName.getData();
                }
            }
        }

        private void notifyUsers(Connection connection, String userName) throws IOException {
            
            for (String otherUserName : connectionMap.keySet()) {
                if (!otherUserName.equals(userName)) {
                    connection.send(new Message(MessageType.USER_ADDED, otherUserName));
                }
            }
        }

        private void serverMainLoop(Connection connection, String userName)
                throws IOException, ClassNotFoundException {

            while (true) {
                Message message = connection.receive();

                if (    message.getType() != null &&
                        message.getType().equals(MessageType.TEXT)
                ) {
                    String text = String.format("%s: %s", userName, message.getData());
                    sendBroadcastMessage(new Message(MessageType.TEXT, text));
                } else {
                    ConsoleHelper.writeMessage("Сообщение не является текстом");
                }
            }
        }

        @Override
        public void run() {

            String userName = null;

            try (
                    Connection connection = new Connection(socket)
            ) {

                ConsoleHelper.writeMessage("Установлено новое соединение: "
                        + connection.getRemoteSocketAddress());

                userName = serverHandshake(connection);

                connectionMap.put(userName, connection);

                for (Connection otherConnection : connectionMap.values())
                    sendBroadcastMessage(new Message(MessageType.USER_ADDED, userName));

                notifyUsers(connection, userName);
                serverMainLoop(connection, userName);

            } catch (IOException | ClassNotFoundException e) {
                ConsoleHelper.writeMessage(e.getMessage());
            }

            if (userName != null) {
                connectionMap.remove(userName);
                sendBroadcastMessage(new Message(MessageType.USER_REMOVED, userName));
            }

            ConsoleHelper.writeMessage(
                    String.format("Соединение %s закрыто...", socket.getRemoteSocketAddress())
            );
        }
    }
}
