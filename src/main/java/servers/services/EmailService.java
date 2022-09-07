package servers.services;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class EmailService {
    private Properties emailProperties;

    public EmailService() throws IOException {
        emailProperties = new Properties();
        emailProperties.load(new FileReader( "src/main/resources/configs/email.properties"));
    }
        public void sendEmailMessage() throws MessagingException {
            Session mailSession = Session.getDefaultInstance(emailProperties);
            MimeMessage message = new MimeMessage(mailSession);
            message.setFrom(new InternetAddress("mr.bublessss@gamil.com"));
            message.addRecipients(Message.RecipientType.TO, "lisitsin.ak@yandex.ru");
            message.setSubject("hello");
            message.setText("111111111111111111");

            Transport transport = mailSession.getTransport();
            transport.connect(null,"udjhamihsrnbsvrx");
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
        }
}
