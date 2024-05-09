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

    @SuppressWarnings("InfiniteLoopStatement")
    public static void main(String[] args) throws IOException {
        store = new MessageStore();
        try (ServerSocket server = new ServerSocket(PORT)) {
            while (true) {
                Socket socket = server.accept();
                try {
                    serverInstances.add(new ServerInstance(socket));
                } catch (IOException e) {
                    socket.close();
                }
            }
        }
    }
}
