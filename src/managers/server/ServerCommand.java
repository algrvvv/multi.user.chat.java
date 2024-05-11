package managers.server;

import main.Server;

import java.io.BufferedReader;
import java.util.Arrays;

public class ServerCommand extends Thread{
    /**
     * Ридер серверных команд
     */
    private final BufferedReader serverCommandReader;

    public ServerCommand(BufferedReader serverCommandReader) {
        this.serverCommandReader = serverCommandReader;
    }

    @Override @SuppressWarnings("InfiniteLoopStatement")
    public void run() {
        while (true) {
            String inputCommand;
            try {
                inputCommand = this.serverCommandReader.readLine();

                if (inputCommand.contains("/")) {
                    this.CommandWrapper(inputCommand.replaceAll("/", ""));
                }

            } catch (Exception ignored) {}
        }
    }

    /**
     * Обработка команд
     *
     * @param command название команды
     */
    private void CommandWrapper(String command) {
        switch (command) {
            case "help": this.executeHelpCommand(); break;
            case "config":  this.executeConfigCommand(); break;
            case "users": this.executeUsersCommand(); break;
            case "clear": this.executeClearCommand(); break;
            default:
                if (command.contains("change-config")) this.executeChangeConfigCommand(command);
                else System.out.println("\n\nНет такой команды\n\n");
                break;
        }
    }

    /**
     * Метод для создания красивого заголовка для команды
     *
     * @param titleForCommand тайтл команды
     */
    private void makeTitleForCommand(String titleForCommand) {
        String frame = "=".repeat(titleForCommand.length());
        System.out.print("\n\n" + frame + "\n" + titleForCommand + "\n" + frame + "\n\n");
    }

    /**
     * Футер для команды (после ее выполнения)
     *
     * @param len длина футера
     */
    private void makeFooterForCommand(int len) {
        System.out.println("=".repeat(len) + "\n\n");
    }

    /* Дальше идут методы предназначенные только для выполнения команд  */

    private void executeHelpCommand() {
        this.makeTitleForCommand("Доступные команды");
        System.out.println("/config - просмотр конфигурации сервера");
        System.out.println("/users - просмотр активных пользователях");
        System.out.println("/clear - очистка терминала (не все терминалы поддерживают)");
        System.out.println("/change-config [option] [value] - переназначение каких либо конфиграций сервера\n");
        this.makeFooterForCommand("Доступные команды".length());
    }

    /**
     * Метод для выполнения команды `config`
     * <p>
     * TODO можно вынести в enum
     */
    private void executeConfigCommand() {
        this.makeTitleForCommand("Конфигурация сервера");
        System.out.println("[port]: Порт работы сервера: " + Server.PORT);
        System.out.println("[max-users]: Максимально кол-во одновременно подключенных пользователей: " + Server.MAX_NUMBER_OF_USERS);
        System.out.println("[store]: Максимальное кол-во сообщений в хранлище: " + Server.MAX_NUMBER_OF_MESSAGE);
        System.out.println("[max-store]: Кол-во сообщений в хранлище: " + Server.store.getNumberOfMessage() + "\n");
        this.makeFooterForCommand("Конфигурация сервера".length());
    }

    /**
     * Метод для выполнения команды `users`
     */
    private void executeUsersCommand() {
        this.makeTitleForCommand("Информация об активных пользователях");
        this.makeFooterForCommand("Информация об активных пользователях".length());
    }

    /**
     * "Очистка" терминала для команды `clear`
     */
    private void executeClearCommand() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    private void executeChangeConfigCommand(String commandString) {
        String[] args = commandString.split(" ");
        if (args.length < 3) {
            System.out.println("Используйте /help, чтобы увидеть доступные команды\n");
            return;
        }

        this.makeTitleForCommand("Изменение конфигцрации сервера");
        switch (args[1]) {
            case "max-users":
                try {
                    int oldValue = Server.MAX_NUMBER_OF_USERS;
                    Server.MAX_NUMBER_OF_USERS = Integer.parseInt(args[2]);
                    System.out.println("Максимальное колво пользователей изменено: " + oldValue + " -> " + args[2] + "\n");
                } catch (Exception e) {
                    System.out.println("Ошибка изменения\n\n");
                }
                break;
            case "max-store":
                try {
                    int oldValue = Server.MAX_NUMBER_OF_MESSAGE;
                    Server.MAX_NUMBER_OF_MESSAGE = Integer.parseInt(args[2]);
                    Server.store.setMaxNumberOfMessage(Integer.parseInt(args[2]));
                    System.out.println("Максимальное колво сообщений в хранилище изменено: " + oldValue + " -> " + args[2] + "\n");
                } catch (Exception e) {
                    System.out.println("Ошибка изменения\n\n");
                }
                break;
            default:
                System.out.println("Недоступная опция для изменения\n");
        }

        this.makeFooterForCommand("Изменение конфигцрации сервера".length());
    }
}
