package managers.server;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.LinkedList;

public class MessageStore {
    /**
     * История сообщения хранит до 10 штук
     */
    private final LinkedList<String> store = new LinkedList<>();

    /**
     * Добавление нового сообщения в историю
     *
     * @param mess новое сообщение
     */
    public void addMessageToStore(String mess) {
        if (this.store.size() > 10) {
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
    public void writeMessage(BufferedWriter writer) {
        if (!this.store.isEmpty()) {
            try {
                writer.write("История сообщений: " + "\n");
                for (String message : this.store) {
                    writer.write(message + "\n");
                }

                writer.write("/..." + "\n");
                writer.flush();
            } catch (IOException ignored) {}
        }
    }

}
