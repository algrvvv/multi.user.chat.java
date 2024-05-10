package main;

import managers.server.MessageStore;
import managers.server.ServerInstance;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

public class Server {
    /**
     * Порт для работа сервера
     */
    public static final int PORT = 8080;

    /**
     * Список всех экземпляров
     */
    public static LinkedList<ServerInstance> serverInstances = new LinkedList<>();

    /**
     * Хранилище последних сообщений
     */
    public static MessageStore store;

    /**
     * Максимально возможное количество одновременно подключенных пользователей
     */
    public static int maxNumberOfUser = 10;

    @SuppressWarnings("InfiniteLoopStatement")
    public static void main(String[] args) throws IOException {
        System.out.println("Запуск севера");
        store = new MessageStore();
        if (args.length > 0) {
           try {
               for (int i = 0; i < args.length; i++) {
                   if (args[i].equals("-m") || args[i].equals("--max")) {
                       maxNumberOfUser = Integer.parseInt(args[++i]);
                   }
               }
           } catch (Exception e) {
               System.out.println("Ошибка конфигурации сервера");
               return;
           }
        }

        try (ServerSocket server = new ServerSocket(PORT)) {
            while (true) {
                Socket socket = server.accept();
                try {
                    if (serverInstances.size() + 1 <= maxNumberOfUser) {
                        serverInstances.add(new ServerInstance(socket));
                    } else {
                        System.out.println("Ошибка подключения нового пользователя в связи с ограничением максимального кол-ва пользователей");
                        socket.close();
                    }
                } catch (IOException e) {
                    socket.close();
                }
            }
        }
    }
}
