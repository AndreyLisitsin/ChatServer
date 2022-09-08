package servers;

import servers.handlers.ChatHistoryHandler;
import servers.handlers.ClientHandler;
import servers.services.AuthenticationService;
import servers.services.impl.DBAuthenticationServiceImpl;
import servers.services.impl.SimpleAuthenticationServiceImpl;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Logger;

public class MyServer {

    private final ServerSocket serverSocket;
    private final AuthenticationService authenticationService;
    private final ArrayList<ClientHandler> clients;
    private static Logger logger = Logger.getLogger("serverLogger");

    public MyServer(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        authenticationService = new DBAuthenticationServiceImpl();
        clients = new ArrayList<>();
    }

    public void start() {
        logger.info("Server start \n------------------- \n");
        try {
            while (true) {
                waitAndProcessNewClientConnetion();
            }
        } catch (IOException e) {
                e.printStackTrace();
        }
    }

    private void waitAndProcessNewClientConnetion() throws IOException {
        System.out.println("Waiting client");
        Socket socket = serverSocket.accept();
        System.out.println("Client connected");

        processClientConnection(socket);
    }

    private void processClientConnection(Socket socket) throws IOException {
        ClientHandler handler = new ClientHandler(this, socket);
        handler.handle();
    }

    public synchronized void subscribe(ClientHandler handler){
        clients.add(handler);
    }


    public synchronized void unSubscribe(ClientHandler handler){
        clients.remove(handler);
        System.out.println(clients);
    }

    public AuthenticationService getAuthenticationService() {
        return authenticationService;
    }

    public boolean isUsernameBusy(String username) {
        for (ClientHandler client: clients) {
            if (client.getUsername().equals(username)){
                return true;
            }
        }
        return false;
    }

    public void stop() {
        logger.info("-------------------------\nЗавершение работы\n");
        System.exit(0);
    }

    public void broadcastMessage(ClientHandler sender, String message) throws IOException {
        for (ClientHandler client: clients){
/*            if (client==sender) {
                continue;
            }*/
            client.sendMessage(sender.getUsername(), message);
        }
    }

    public void broadcastPrivateMessage(ClientHandler recipient, String message) throws IOException {
        String recipientName = message.split("\\s+")[1];
        logger.info("Отправка сообещения пользователю: " + recipientName);
        for (ClientHandler client : clients){
            if (client.getUsername().equals(recipientName)){
                client.sendPrivateMessage(recipient.getUsername(), message);
                break;
            }
        }
    }

    public synchronized void createNewUser(String userParameters){
        authenticationService.createNewUSer(userParameters);
    }


    public String getAllUsers() {
        StringBuilder b = new StringBuilder();
        b.append("list ");

        for (int i = 0; i < clients.size(); i++){
            b.append(clients.get(i).getUsername()).append(" ");
        }
       // System.out.println(b);
        return b.toString();

    }

    public void broadcastUpdate(ClientHandler sender, String message) throws IOException {
        for (ClientHandler client: clients){
            if (client==sender) {
                client.updateUser(message);
            }
        }
    }

}
