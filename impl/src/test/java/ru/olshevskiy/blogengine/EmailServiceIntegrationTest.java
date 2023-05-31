package ru.olshevskiy.blogengine;

import static org.assertj.core.api.Assertions.assertThat;

import com.icegreen.greenmail.configuration.GreenMailConfiguration;
import com.icegreen.greenmail.junit5.GreenMailExtension;
import com.icegreen.greenmail.util.ServerSetupTest;
import java.util.concurrent.TimeUnit;
import javax.mail.Multipart;
import javax.mail.internet.MimeMessage;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.test.context.ActiveProfiles;
import ru.olshevskiy.blogengine.service.EmailService;

/**
 * EmailServiceIntegrationTest.
 *
 * @author Sergey Olshevskiy
 */
@SpringBootTest
@ActiveProfiles("test")
public class EmailServiceIntegrationTest {

  @RegisterExtension
  static GreenMailExtension greenMail = new GreenMailExtension(ServerSetupTest.SMTP)
          .withConfiguration(GreenMailConfiguration.aConfig().withUser("sender@test.com", "pass"))
          .withPerMethodLifecycle(false);

  @Autowired
  private EmailService emailService;
  @Autowired
  private JavaMailSenderImpl mailSender;

  @BeforeEach
  void setup() {
    mailSender.setHost("127.0.0.1");
    mailSender.setPort(3025);
  }

  @Test
  void testSendMessage() {
    emailService.sendMessage("recipient@test.com", "subject", "text");

    Awaitility.await().atMost(2, TimeUnit.SECONDS).untilAsserted(() -> {
      MimeMessage receivedMessage = greenMail.getReceivedMessages()[0];
      assertThat(receivedMessage.getAllRecipients().length).isEqualTo(1);
      assertThat(receivedMessage.getAllRecipients()[0].toString()).isEqualTo("recipient@test.com");
      assertThat(receivedMessage.getFrom()[0].toString()).isEqualTo("sender@test.com");
      assertThat(receivedMessage.getSubject()).isEqualTo("subject");

      Multipart content = (Multipart) greenMail.getReceivedMessages()[0].getContent();
      assertThat(content.getBodyPart(0).getContent().toString()).isEqualTo("text");
    });
  }
}