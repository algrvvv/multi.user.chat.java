package main;

import managers.server.MessageStore;
import managers.server.ServerInstance;

import java.util.LinkedList;

public class Server {
    /**
     * Список всех экземпляров
     */
    public static LinkedList<ServerInstance> serverInstances = new LinkedList<>();

    /**
     * Хранилище последних сообщений
     */
    public static MessageStore store;
}
