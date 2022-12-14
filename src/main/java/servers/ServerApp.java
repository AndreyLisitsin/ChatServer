package servers;

import servers.services.EmailService;

import javax.mail.MessagingException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

public class ServerApp {
    private static final int DEFAULT_PORT = 8086;

    private static String configsFile = "src/main/resources/configs/server-configs.properties";

    public static void main(String[] args) throws IOException, MessagingException {

        Properties properties = new Properties();

        try {
            properties.load(new FileReader(configsFile));

        } catch (IOException e) {
            e.printStackTrace();
        }

        int port;
        try{
            port = Integer.parseInt(properties.getProperty("server.port"));
        }  catch (NumberFormatException e){
            e.printStackTrace();
            port = DEFAULT_PORT;
        }

        try {
            new MyServer(port).start();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

}
