package com.duoxik.chatbox.client;

import com.duoxik.chatbox.ConsoleHelper;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class BotClient extends Client {

    @Override
    protected String getUserName() {
        return String.format("date_bot_%d", (int) (Math.random() * 100));
    }

    @Override
    protected boolean shouldSendTextFromConsole() {
        return false;
    }

    @Override
    protected SocketThread getSocketThread() {
        return new BotSocketThread();
    }

    public class BotSocketThread extends Client.SocketThread {

        private Map<String, String> datePatternsMap = new HashMap<>();

        {
            datePatternsMap.put("дата", "d.MM.YYYY");
            datePatternsMap.put("день", "d");
            datePatternsMap.put("месяц", "MMMM");
            datePatternsMap.put("год", "YYYY");
            datePatternsMap.put("время", "H:mm:ss");
            datePatternsMap.put("час", "H");
            datePatternsMap.put("минуты", "m");
            datePatternsMap.put("секунды", "s");
        }

        @Override
        protected void processIncomingMessage(String message) {

            ConsoleHelper.writeMessage(message);
            if (!message.contains(":")) return;

            int splitterIndex = message.indexOf(":");
            String name = message.substring(0, splitterIndex);
            String command = message.substring(splitterIndex + 2);
            String datePattern = datePatternsMap.get(command);

            if (datePattern != null) {
                sendTextMessage(
                        String.format("Информация для %s: %s", name,
                                new SimpleDateFormat(datePattern).format(new Date()))
                );
            }
        }

        @Override
        protected void clientMainLoop() throws IOException, ClassNotFoundException {
            sendTextMessage("Привет чатику. Я бот. Понимаю команды: дата, день, месяц, " +
                    "год, время, час, минуты, секунды.");
            super.clientMainLoop();
        }
    }

    public static void main(String[] args) {
        new BotClient().run();
    }
}
