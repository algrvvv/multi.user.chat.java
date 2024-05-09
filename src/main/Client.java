package main;

import managers.client.ClientInstance;

public class Client {
    /**
     * Адрес работы клиента
     */
    public static String ipAddress = "localhost";

    /**
     * Порт для работы клиента
     */
    public static int port = 8080;

    /**
     * Запуск клиента
     *
     * @param args аргументы
     */
    public static void main(String[] args) {
        new ClientInstance(port, ipAddress);
    }
}
