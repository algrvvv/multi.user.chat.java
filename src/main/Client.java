package main;

import managers.client.ClientInstance;

import java.util.Arrays;

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
        if (args.length > 0) {
            try {
                for (int i = 0; i < args.length; i++) {
                    if (args[i].equals("-a") || args[i].equals("--address")) {
                        ipAddress = args[++i];
                    } else if (args[i].equals("-p") || args[i].equals("--port")) {
                        port = Integer.parseInt(args[++i]);
                    }
                }
            } catch (Exception e) {
                System.out.println("Вы использовали недопустимые аргументы или недопустимое колво аргументов");
                return;
            }

        }

        new ClientInstance(port, ipAddress);
    }
}
