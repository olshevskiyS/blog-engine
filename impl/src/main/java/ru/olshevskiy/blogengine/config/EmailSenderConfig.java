package ru.olshevskiy.blogengine.config;

import java.util.Properties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSenderImpl;

/**
 * EmailSenderConfig.
 *
 * @author Sergey Olshevskiy
 */
@Configuration
public class EmailSenderConfig {

  @Value("${spring.mail.username}")
  private String serverAddress;

  @Value("${spring.mail.password}")
  private String serverAddressPassword;

  @Value("${spring.mail.port}")
  private Integer port;

  @Value("${spring.mail.host}")
  private String host;

  /**
   * JavaMailSender bean.
   */
  @Bean
  public JavaMailSenderImpl mailSender(Properties mailProperties) {
    JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
    mailSender.setUsername(serverAddress);
    mailSender.setPassword(serverAddressPassword);
    mailSender.setJavaMailProperties(mailProperties);
    return mailSender;
  }

  /**
   * JavaMailSender mailProperties bean.
   */
  @Bean
  public Properties mailProperties() {
    Properties properties = new Properties();
    properties.put("mail.smtp.host", host);
    properties.put("mail.smtp.auth", "true");
    properties.put("mail.smtp.socketFactory.port", port);
    properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
    return properties;
  }
}