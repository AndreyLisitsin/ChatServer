import org.junit.jupiter.api.Test;
import servers.services.EmailService;
import java.io.IOException;


public class EmailTest {

    private final EmailService emailService;

    public EmailTest() throws IOException {
        emailService = EmailService.createInstance();
    }
    @Test
    public void sendMessageException(){
        emailService.sendEmailMessage("sdferghrthhvbnvb");
    }

    @Test
    public void sendMessage(){
        emailService.sendEmailMessage("lisitsin.ak@yandex.ru");
    }
}
