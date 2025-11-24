package com.fastbank.account_service.email;

import com.fastbank.account_service.model.dto.PersonRecord;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.time.Year;

@Service
public class EmailService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    public EmailService(JavaMailSender mailSender, TemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

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
