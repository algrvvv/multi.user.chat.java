package managers.server;

import main.Server;

import java.io.*;
import java.net.Socket;

public class ServerInstance extends Thread {
    /**
     * Сокет для каждого экземпляра
     */
    private final Socket socket;

    /**
     * Поток для записи в сокет
     */
    private final BufferedWriter writer;

    /**
     * Поток для чтения из сокета
     */
    private final BufferedReader reader;

    /**
     * Имя пользователя, который пользуется этим потоком для чата
     */
    private String userNickName;

    /**
     * Конструтор для экземпляра
     *
     * @param socket сокет
     */
    public ServerInstance(Socket socket) throws IOException {
        this.socket = socket;

        this.writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "Cp866"));
        this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "Cp866"));

        Server.store.printMessageStore(writer);
        System.out.println("К чату подключается новый пользователь!");

        // запуск нити
        this.start();
    }

    @Override
    public void run() {
        String word;
        try {
            word = reader.readLine();
            this.userNickName =  word.replaceAll("Добро пожаловать, ", "").replaceAll("!", "");

            try {
                writer.write(word + "\n");
                writer.flush();

                for (ServerInstance si : Server.serverInstances) {
                    System.out.println("! Пользователь " + userNickName + " присоединился к чату!");
                    Server.store.addMessageToStore("> Пользователь " + userNickName + " присоединился к чату!");
                    si.sendMessage("! Пользователь " + userNickName + " присоединился к чату!");
                }
            } catch (IOException ignored) {}

            try {
                while (true) {
                    word = reader.readLine();
                    if (word.equals("exit")) { this.downServerInstance(); break; }

                    System.out.println("Сообщение: " + word + " -> ОТ: " + userNickName);
                    Server.store.addMessageToStore(word);
                    for (ServerInstance si : Server.serverInstances) {
                        si.sendMessage(word);
                    }
                }
            } catch (NullPointerException ignored) {}
        } catch (IOException e) {
            this.downServerInstance();
        }
    }

    public void sendMessage(String message) {
        try {
            writer.write(message + "\n");
            writer.flush();
        } catch (IOException ignored) {}
    }

    public void downServerInstance() {
        try {
            if (!socket.isClosed()) {
                socket.close(); reader.close(); writer.close();
                for (ServerInstance si : Server.serverInstances) {
                    if (si.equals(this)) si.interrupt();
                    Server.serverInstances.remove(this);
                }
            }
        } catch (IOException ignored) {}
    }

    /**
     * Геттер на никнейм
     *
     * @return никнейм
     */
    public String getUserNickName() {
        return this.userNickName;
    }
}
