package managers.client;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ClientInstance {
    /**
     * Сокет для клиента
     */
    private Socket socket;

    /**
     * Чтение с сокета
     */
    private BufferedReader reader;

    /**
     * Запись в сокет
     */
    private BufferedWriter writer;

    /**
     * Чтение пользовательского ввода
     */
    private BufferedReader userReader;

    /**
     * Имя пользователя
     */
    private String userName;

    public ClientInstance(int port, String ipAddress) {
        try {
            this.socket = new Socket(ipAddress, port);
        } catch (IOException e) {
            System.out.println("Ошибка создания клиентского сокета");
        }

        try {
            userReader = new BufferedReader(new InputStreamReader(System.in, "Cp866"));
            reader  = new BufferedReader(new InputStreamReader(socket.getInputStream(), "Cp866"));
            writer  = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "Cp866"));

            this.getUserName();
            new MessageReader().start();
            new MessageWriter().start();
        } catch (IOException e) {
            ClientInstance.this.downService();
        }
    }

    private void getUserName() {
        System.out.println("Введите свое имя: ");
        try {
            userName = userReader.readLine();
            writer.write("Добро пожаловать, " + userName + "!\n");
            writer.flush();
        } catch (IOException ignored) {}
    }

    public void downService() {
        try {
            if (!socket.isClosed()) {
                socket.close(); reader.close();
                writer.close();
            }
        } catch (IOException ignored) {}
    }

    /**
     * Вложенный класс для отправки сообщений
     */
    public class MessageWriter extends Thread {
        @Override
        public void run() {
            while (true) {
                String inputMessage;

                try {
                    String strTime = new SimpleDateFormat("HH:mm:ss").format(new Date());
                    inputMessage = userReader.readLine();

                    if (inputMessage.equals("exit")) {
                        writer.write("exit" + "\n");
                        ClientInstance.this.downService();
                        break;
                    } else {
                        writer.write("(" + strTime + ") " + userName + ": " + inputMessage + "\n");
                    }

                    writer.flush();

                } catch (IOException e) {
                    ClientInstance.this.downService();
                }

            }
        }
    }

    /**
     * Вложенный класс для чтения сообщений
     */
    private class MessageReader extends Thread {
        @Override
        public void run() {
            String str;
            try {
                while (true) {
                    str = reader.readLine();
                    if (str.equals("exit")) {
                        ClientInstance.this.downService();
                        break;
                    }

                    System.out.println(str);
                }
            } catch (IOException e) {
                ClientInstance.this.downService();
            }
        }
    }
}
