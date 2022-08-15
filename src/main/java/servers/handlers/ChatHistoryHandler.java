package servers.handlers;

import java.io.*;
import java.sql.Struct;
import java.util.Date;

public class ChatHistoryHandler {

    private File file;
    private FileWriter writer;
    private BufferedInputStream inputStream;
    private BufferedReader reader;


    public ChatHistoryHandler() throws IOException {
       // file = new File("C:\\MyProjects\\Server_for_Chat\\src\\main\\resources\\chatHostory\\chatHistory.txt");
        writer = new FileWriter("C:\\MyProjects\\Server_for_Chat\\src\\main\\resources\\chatHostory\\chatHistory.txt", true);
        inputStream = new BufferedInputStream(new FileInputStream("C:\\MyProjects\\Server_for_Chat\\src\\main\\resources\\chatHostory\\chatHistory.txt"));
        reader = new BufferedReader(new InputStreamReader(inputStream));
    }


    public void writeToHistory(String message) throws IOException {

        writer.append(message).append("\n");
        writer.flush();
    }

    public String readFromHistory() throws IOException {
        int i=0;
        int a;
        StringBuilder builder = new StringBuilder();
        String s;

        while ((s = reader.readLine()) != null){

            if ( i > 99 )
                break;

                builder.append(s).append("\n");
                i++;

        }


/*        while ((a = inputStream.read()) != -1){
            builder.append((char) a);
        }*/
        System.out.println("отправил историю" + builder);
        return builder.toString();
    }
}
