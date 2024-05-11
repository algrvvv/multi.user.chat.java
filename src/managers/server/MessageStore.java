package managers.server;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.LinkedList;

public class MessageStore {
    /**
     * История сообщения хранит до 10 штук
     */
    private final LinkedList<String> store = new LinkedList<>();

    private int MAX_NUMBER_OF_MESSAGE = 10;

    public MessageStore() {
    }

    public MessageStore(int maxNumberOfMessage) {
        this.MAX_NUMBER_OF_MESSAGE = maxNumberOfMessage;
    }

    /**
     * Добавление нового сообщения в историю
     *
     * @param mess новое сообщение
     */
    public void addMessageToStore(String mess) {
        if (this.store.size() > this.MAX_NUMBER_OF_MESSAGE) {
            this.store.removeFirst();
            this.store.add(mess);
        } else {
            this.store.add(mess);
        }
    }


    /**
     * Отправка истории сообщений
     *
     * @param writer поток вывода для каждого клиента
     */
    public void printMessageStore(BufferedWriter writer) {
        if (!this.store.isEmpty()) {
            try {
                writer.write("История сообщений: " + "\n");
                for (String message : this.store) {
                    writer.write(message + "\n");
                }

                writer.write("\n");
                writer.flush();
            } catch (IOException ignored) {}
        }
    }

    /**
     * Геттер для колва сообщений
     *
     * @return колво сохранненых сообщений
     */
    public int getNumberOfMessage() {
        return this.store.size();
    }

}
