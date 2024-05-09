package managers.server;

import main.Server;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

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
     * Конструтор для экземпляра
     *
     * @param socket сокет
     */
    public ServerInstance(Socket socket) throws IOException {
        this.socket = socket;

        this.writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "Cp866"));
        this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "Cp866"));

        Server.store.printMessageStore(writer);
        System.out.println("К чату подключился новый пользователь!");

        // запуск нити
        this.start();
    }

    @Override
    public void run() {
        String word;
        try {
            word = reader.readLine();
            try {
                writer.write(word + "\n");
                writer.flush();
            } catch (IOException ignored) {}

            try {
                while (true) {
                    word = reader.readLine();
                    if (word.equals("exit")) {
                        this.downServerInstance();
                        break;
                    }

                    System.out.println("Сообщение: " + word);
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

    private void downServerInstance() {
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
}
