package servers.handlers;

import servers.MyServer;
import servers.services.AuthenticationService;

import java.io.*;
import java.net.Socket;
import java.util.Date;

public class ClientHandler {

    private static final String AUTH_CMD_PREFIX = "/auth";
    private static final String AUTHOK_CMD_PREFIX ="/authok";
    private static final String AUTHERR_CMD_PREFIX = "/autherr";
    private static final String CLIENT_MSG_CMD_PREFIX = "/cMsg";
    private static final String SERVER_MSG_CMD_PREFIX = "/sMsg";
    private static final String PRIVATE_MSG_CMD_PREFIX = "/pm";
    private static final String STOP_SERVER_CMD_PREFIX = "/stop";
    private static final String END_CLIENT_CMD_PREFIX = "/end";
    private static final String END_UNSUBSCRIBE_PREFIX = "/unsub";
    private static final String REG_NEWUSER_PREFIX = "/reg";
    private static final String UPLOAD_USER_LIST = "/uploadUsersList";
    private static final String UPDATE_USER_PREFIX = "/updateUser";


    private MyServer myServer;
    private Socket clientSocket;
    private DataInputStream in;
    private DataOutputStream out;
    private String username;
    private static File file;
    private static FileWriter fileWriter;
 //   private static FileReader fileReader;
    private static BufferedInputStream inputStream;
    private final ChatHistoryHandler chatHandler;


    public ClientHandler(MyServer myServer, Socket socket) throws IOException {
        chatHandler = new ChatHistoryHandler();
        this.myServer = myServer;
        clientSocket = socket;

    }

    public void handle() throws IOException {
        in = new DataInputStream(clientSocket.getInputStream());
        out = new DataOutputStream(clientSocket.getOutputStream());

        new Thread(()->{
            try {
                authentication();
                readMessage();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }


    private void authentication() throws IOException {
        while (true){
            String message = in.readUTF();

            if(message.startsWith(REG_NEWUSER_PREFIX)){
                myServer.createNewUser(message);
                continue;
            }
            if (message.startsWith(AUTH_CMD_PREFIX)){
                boolean isSuccessAuth = processAuthentication(message);
                if (isSuccessAuth){
                    break;
                }
            }
            else {
                out.writeUTF(AUTHERR_CMD_PREFIX + " Неверная команда аутентификации");
                System.out.println(" Неверная команда аутентификации");
            }

        }
    }

    private boolean processAuthentication(String message) throws IOException {

        String[] messageParts = message.split("\\s+");
        if (messageParts.length != 3){
            out.writeUTF(AUTHERR_CMD_PREFIX + " Неверная команда аутентификации");
            System.out.println(" Неверная команда аутентификации");
            return false;
        }

        String login = messageParts[1];
        String password = messageParts[2];

        AuthenticationService auth = myServer.getAuthenticationService();

        username = auth.getUsernameByLoginAndPassword(login, password);

        if(username != null){
            if (myServer.isUsernameBusy(username)){
                out.writeUTF(AUTHERR_CMD_PREFIX + "Login is already using ");
                return false;
            }
            out.writeUTF(AUTHOK_CMD_PREFIX+ " " + username+" " + sendChatHistory());
            myServer.subscribe(this);
            System.out.println("Пользователь "+ username +" подключился к чату");
            return true;
        } else {
            out.writeUTF(AUTHERR_CMD_PREFIX + " Неверная комбинация логина и пароля");
            return false;
        }
    }

    private void readMessage() throws IOException {
        while (true){
            String message = in.readUTF();
            System.out.println("message " + username + ": " + message);
            String typeMessage = message.split("\\s+")[0];
            switch (typeMessage) {
                case STOP_SERVER_CMD_PREFIX: myServer.stop();
                case END_CLIENT_CMD_PREFIX: closeConnection();
                break;
                case PRIVATE_MSG_CMD_PREFIX: myServer.broadcastPrivateMessage(this, message);
                break;
                case END_UNSUBSCRIBE_PREFIX: myServer.unSubscribe(this);
                case UPLOAD_USER_LIST: uploadUsersList();
                break;
                case UPDATE_USER_PREFIX:myServer.broadcastUpdate(this, message);
                break;
                default: myServer.broadcastMessage(this, message);

            }
        }
    }

    public void closeConnection() throws IOException {
        myServer.unSubscribe(this);
        clientSocket.close();
        System.out.println(username + " disconnected");
    }

    public void sendMessage(String sender, String message) throws IOException {

        chatHandler.writeToHistory(String.format("%s, %s: %s", sender, new Date().toString(), message));

        if (this.getUsername().equals(sender)){
            out.writeUTF(String.format("Я, %s: %s", new Date().toString(), message));
        }
        else {
            out.writeUTF(String.format("%s, %s: %s", sender, new Date().toString(), message));
        }
    }

    public void sendPrivateMessage(String recipient, String message) throws IOException {
        out.writeUTF(String.format("%s %s %s", PRIVATE_MSG_CMD_PREFIX, recipient, message));
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "ClientHandler{" +
                "username='" + username + '\'' +
                '}';
    }

    public void uploadUsersList(){
        try {
            out.writeUTF(myServer.getAllUsers());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateUser(String message) throws IOException {
        String newUsername = message.split("\\s+")[1];
        myServer.getAuthenticationService().updateUsernameByNickname(this, newUsername);
        out.writeUTF(String.format("/upNameOk %s", newUsername));
    }

    public String sendChatHistory() throws IOException {
        return chatHandler.readFromHistory();
    }

}
