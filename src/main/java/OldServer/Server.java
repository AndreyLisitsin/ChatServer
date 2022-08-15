package OldServer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Locale;

public class Server {
    private static final int SERVER_PORT = 8186;
    private static DataInputStream in;
    private static DataOutputStream out;
    private static Socket clientSocket;

    public static void main(String[] args) {

        try(ServerSocket serverSocket = new ServerSocket(SERVER_PORT)){

            while (true) {
                System.out.println("Waiting for connection...");
                clientSocket = serverSocket.accept();
                System.out.println("Connection success!");

                in = new DataInputStream(clientSocket.getInputStream());
                out = new DataOutputStream(clientSocket.getOutputStream());

                try {
                    while (true) {
                        String message = in.readUTF();

                        if(message.equals("/stop")){
                            System.out.println("OldServer.Server stopped");
                            System.exit(0);
                        }
                        System.out.println("Client: " + message);
                        out.writeUTF(message.toUpperCase());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                finally {
                        try {
                            clientSocket.close();
                        } catch (IOException e){
                            e.printStackTrace();
                        }
                    }
                }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
