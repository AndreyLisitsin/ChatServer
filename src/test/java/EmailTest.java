import org.junit.jupiter.api.Test;
import servers.services.EmailService;

import javax.mail.MessagingException;
import java.io.IOException;


public class EmailTest {

    private final EmailService emailService;

    public EmailTest() throws IOException {
        emailService = new EmailService();
    }
    @Test
    public void sendTestMessage() throws MessagingException {
        emailService.sendEmailMessage();
    }
}
