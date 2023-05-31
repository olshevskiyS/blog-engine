package ru.olshevskiy.blogengine.service;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

/**
 * EmailService.
 *
 * @author Sergey Olshevskiy
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

  private final JavaMailSenderImpl mailSender;

  /**
   * EmailService. Send a message for email method.
   */
  public void sendMessage(String recipientEmail, String subject, String text) {
    log.info("Start of the formation of a message to " + recipientEmail);
    MimeMessage message = mailSender.createMimeMessage();
    try {
      message.setFrom(mailSender.getUsername());
      message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipientEmail));
      message.setSubject(subject);
      message.setContent(formMessageContent(text));
      mailSender.send(message);
    } catch (MessagingException ex) {
      log.warn("Error sending email " + recipientEmail + " by cause:/n" + ex);
    }
    log.info("The email was successfully sent to " + recipientEmail);
  }

  private Multipart formMessageContent(String text) throws MessagingException {
    Multipart multipart = new MimeMultipart();
    MimeBodyPart bodyPart = new MimeBodyPart();
    bodyPart.setText(text, "UTF-8", "html");
    bodyPart.setDisposition(BodyPart.INLINE);
    multipart.addBodyPart(bodyPart);
    return multipart;
  }
}