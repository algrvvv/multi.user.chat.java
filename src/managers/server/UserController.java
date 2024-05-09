package managers.server;

import main.Server;

public class UserController {
    public static void newUser(String userName) {
        for (ServerInstance si : Server.serverInstances) {
            si.sendMessage("В чат вошел " + userName);
        }
    }

}
