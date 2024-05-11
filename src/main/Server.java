package main;

import managers.server.MessageStore;
import managers.server.ServerCommand;
import managers.server.ServerInstance;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
     * Чтение команд на сервере
     */
    private static BufferedReader serverCommandReader;

    /**
     * Максимально возможное количество одновременно подключенных пользователей
     */
    public static int MAX_NUMBER_OF_USERS = 10;

    /**
     * Максимально возможное количество сообщения в хранилище
     */
    public static int MAX_NUMBER_OF_MESSAGE = 10;

    /**
     * Колво активных пользователей
     */
    private volatile static int numberOfUser = 0;

    @SuppressWarnings("InfiniteLoopStatement")
    public static void main(String[] args) throws IOException {
        System.out.println("Запуск севера");
        serverCommandReader = new BufferedReader(new InputStreamReader(System.in, "Cp866"));
        store = new MessageStore(MAX_NUMBER_OF_MESSAGE);

        if (args.length > 0) {
           try {
               for (int i = 0; i < args.length; i++) {
                   if (args[i].equals("-m") || args[i].equals("--max")) {
                       MAX_NUMBER_OF_USERS = Integer.parseInt(args[++i]);
                   }
               }
           } catch (Exception e) {
               System.out.println("Ошибка конфигурации сервера");
               return;
           }
        }

        new ServerCommand(Server.serverCommandReader).start();

        try (ServerSocket server = new ServerSocket(PORT)) {
            while (true) {
                Socket socket = server.accept();
                try {
                    if (numberOfUser < MAX_NUMBER_OF_USERS) {
                        serverInstances.add(new ServerInstance(socket));
                        incrementNumberOfUser();
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

    /**
     * Получение числа активных пользователей
     *
     * @return колво активных пользователей
     */
    public static int getNumberOfUser() {
        return numberOfUser;
    }

    /**
     * Метод для увеличения колва активных пользователей на одного
     */
    public static synchronized void incrementNumberOfUser() {
        numberOfUser += 1;
    }

    /**
     * Метод для уменьшнения колва активных пользователей на одного
     */
    public static synchronized void decrementNumberOfUser() {
        numberOfUser -= 1;
    }
}
