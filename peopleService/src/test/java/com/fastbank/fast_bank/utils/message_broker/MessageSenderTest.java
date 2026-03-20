package com.fastbank.fast_bank.utils.message_broker;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

import com.fastbank.fast_bank.model.entity.Person;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

@ExtendWith(MockitoExtension.class)
class MessageSenderTest {

    @Mock private RabbitTemplate rabbitTemplate;

    @InjectMocks private MessageSender messageSender;

    private final Person person =
            Person.builder()
                    .personId(UUID.randomUUID())
                    .firstName("John")
                    .lastName("Doe")
                    .email("john@example.com")
                    .build();

    // ── sendMessage ───────────────────────────────────────────────────────────

    @Test
    void sendMessage_shouldPublishToCorrectExchangeAndRoutingKey() {
        messageSender.sendMessage(person);

        verify(rabbitTemplate).convertAndSend(eq("people-event"), eq("people.created"), eq(person));
    }

    @Test
    void sendMessage_shouldPublishExactPersonObject() {
        messageSender.sendMessage(person);

        verify(rabbitTemplate)
                .convertAndSend(RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.ROUTING_KEY, person);
        verifyNoMoreInteractions(rabbitTemplate);
    }

    @Test
    void sendMessage_whenRabbitTemplateFails_shouldPropagateException() {
        doThrow(new RuntimeException("broker unavailable"))
                .when(rabbitTemplate)
                .convertAndSend(any(), any(), any(Object.class));

        assertThatThrownBy(() -> messageSender.sendMessage(person))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("broker unavailable");
    }
}
