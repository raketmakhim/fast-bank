package com.fastbank.accountservice.email;

import com.fastbank.accountservice.model.dto.PersonRecord;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.time.Year;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

/**
 * Service responsible for sending transactional emails via JavaMail and Thymeleaf templates.
 */
@Service
public class EmailService {

  private final JavaMailSender mailSender;
  private final TemplateEngine templateEngine;

  /**
   * Constructs an {@code EmailService} with the given mail sender and template engine.
   *
   * @param mailSender     the JavaMail sender used to dispatch emails
   * @param templateEngine the Thymeleaf engine used to render HTML email templates
   */
  public EmailService(JavaMailSender mailSender, TemplateEngine templateEngine) {
    this.mailSender = mailSender;
    this.templateEngine = templateEngine;
  }

  /**
   * Sends a welcome email to the person whose account was just created.
   *
   * <p>Renders the {@code email/account-created.html} Thymeleaf template with the person's
   * details and dispatches it to the person's email address.
   *
   * @param person the {@link PersonRecord} containing the recipient's name and email
   * @throws RuntimeException if the email could not be constructed or sent
   */
  public void sendAccountCreatedEmail(PersonRecord person) {
    try {
      MimeMessage message = mailSender.createMimeMessage();
      MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

      helper.setFrom("no-reply@fastbank.com");
      helper.setTo(person.email());
      helper.setReplyTo("noreply@yourdomain.com");
      helper.setSubject("Welcome! Your Account Has Been Created");

      Context context = new Context();
      context.setVariable("firstName", person.firstName());
      context.setVariable("lastName", person.lastName());
      context.setVariable("loginUrl", "https://fastbank.com/login");
      context.setVariable("year", Year.now().getValue());

      String htmlContent = templateEngine.process("email/account-created.html", context);
      helper.setText(htmlContent, true);

      mailSender.send(message);
    } catch (MessagingException e) {
      throw new RuntimeException(e);
    }
  }
}
