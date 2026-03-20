package com.fastbank.account_service.email;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import com.fastbank.account_service.model.dto.PersonRecord;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {

    @Mock private JavaMailSender mailSender;

    @Mock private TemplateEngine templateEngine;

    @InjectMocks private EmailService emailService;

    private final PersonRecord person =
            new PersonRecord("John", "Doe", "john@example.com", UUID.randomUUID());

    // ── sendAccountCreatedEmail ───────────────────────────────────────────────

    @Test
    void sendAccountCreatedEmail_shouldCreateAndSendMimeMessage() throws MessagingException {
        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        when(templateEngine.process(any(String.class), any(Context.class)))
                .thenReturn("<html>Welcome John</html>");

        emailService.sendAccountCreatedEmail(person);

        verify(mailSender).send(mimeMessage);
    }

    @Test
    void sendAccountCreatedEmail_shouldProcessCorrectTemplate() {
        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        when(templateEngine.process(any(String.class), any(Context.class)))
                .thenReturn("<html></html>");

        emailService.sendAccountCreatedEmail(person);

        verify(templateEngine).process(eq("email/account-created.html"), any(Context.class));
    }

    @Test
    void sendAccountCreatedEmail_shouldPassPersonVariablesToTemplate() {
        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        ArgumentCaptor<Context> contextCaptor = ArgumentCaptor.forClass(Context.class);
        when(templateEngine.process(any(String.class), contextCaptor.capture()))
                .thenReturn("<html></html>");

        emailService.sendAccountCreatedEmail(person);

        Context capturedContext = contextCaptor.getValue();
        assertThat(capturedContext.getVariable("firstName")).isEqualTo("John");
        assertThat(capturedContext.getVariable("lastName")).isEqualTo("Doe");
        assertThat(capturedContext.getVariable("year")).isNotNull();
    }

    @Test
    void sendAccountCreatedEmail_whenMessagingExceptionOccurs_shouldThrowRuntimeException() {
        // MimeMessage that throws when helper tries to set fields on it
        MimeMessage brokenMessage =
                mock(
                        MimeMessage.class,
                        invocation -> {
                            throw new MessagingException("SMTP error");
                        });
        when(mailSender.createMimeMessage()).thenReturn(brokenMessage);

        assertThatThrownBy(() -> emailService.sendAccountCreatedEmail(person))
                .isInstanceOf(RuntimeException.class)
                .hasCauseInstanceOf(MessagingException.class);

        verify(mailSender, never()).send(any(MimeMessage.class));
    }
}
