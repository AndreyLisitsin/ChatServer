package servers.services;

import com.sun.mail.util.MailConnectException;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.*;

public class EmailService {
    private static final Logger logger = Logger.getLogger("emailLogger");
    private static Handler logHandler;
    private static Properties emailProperties;
    private Transport transport;
    private static EmailService emailService;

/*    public EmailService() throws IOException {
        emailProperties = new Properties();
        emailProperties.load(new FileReader( "src/main/resources/configs/email.properties"));
        logHandler = new FileHandler("src/main/resources/logs/EmailLog.log");
        logger.addHandler(logHandler);
        logHandler.setFormatter(new Formatter() {
            @Override
            public String format(LogRecord record) {
                return String.format("%s\t%s\t%s\t%s\n",
                        record.getLevel(),
                        new Date(record.getMillis()),
                        record.getMessage(),
                        record.getSourceClassName());
            }
        });
    }*/

    public static EmailService createInstance(){
        if (emailService != null){
            return emailService;
        }
        else {
            emailService = new EmailService();
            emailProperties = new Properties();
            try {
                emailProperties.load(new FileReader( "src/main/resources/configs/email.properties"));
                logHandler = new FileHandler("src/main/resources/logs/EmailLog.log");
            } catch (IOException e) {
                e.printStackTrace();
            }
            logger.addHandler(logHandler);
            logHandler.setFormatter(new Formatter() {
                @Override
                public String format(LogRecord record) {
                    return String.format("%s\t%s\t%s\t%s\n",
                            record.getLevel(),
                            new Date(record.getMillis()),
                            record.getMessage(),
                            record.getSourceClassName());
                }
            });
            return emailService;
        }
    }
    public void sendEmailMessage(String email) {
        Session mailSession = Session.getDefaultInstance(emailProperties);
        MimeMessage message = new MimeMessage(mailSession);

        try {
            message.setFrom(new InternetAddress("mr.bublessss@gmail.com"));
            message.addRecipients(Message.RecipientType.TO, email);
            message.setSubject("Confirmation of registration");
            int regCode = ThreadLocalRandom.current().nextInt(1000, 9999);
            message.setText(String.valueOf(regCode));

            transport = mailSession.getTransport();
            transport.connect(null,"6548798713213");
            transport.sendMessage(message, message.getAllRecipients());
            logger.info(String.format("Отправлено письмо для регистрации на почту %s, ", email));
        } catch (MessagingException e) {
                logger.warning("Ошибка отправки сообщения ");
            }
            finally {
                try {
                    if (transport != null)
                        transport.close();
                    } catch (MessagingException e) {
                        logger.warning("Ошибка закрытия transport");
                    }
        }
    }
}
