package com.fastbank.fastbank.utils.messagebroker;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import com.fastbank.fastbank.model.entity.Person;
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
  void sendMessagePublishesToCorrectExchangeAndKey() {
    messageSender.sendMessage(person);

    verify(rabbitTemplate).convertAndSend(eq("people-event"), eq("people.created"), eq(person));
  }

  @Test
  @SuppressWarnings("PMD.UnitTestContainsTooManyAsserts")
  void sendMessagePublishesExactPersonObject() {
    messageSender.sendMessage(person);

    verify(rabbitTemplate)
        .convertAndSend(RabbitMqConfig.EXCHANGE_NAME, RabbitMqConfig.ROUTING_KEY, person);
    verifyNoMoreInteractions(rabbitTemplate);
  }

  @Test
  void sendMessageWhenBrokerFailsPropagatesException() {
    doThrow(new RuntimeException("broker unavailable"))
        .when(rabbitTemplate)
        .convertAndSend(any(), any(), any(Object.class));

    assertThatThrownBy(() -> messageSender.sendMessage(person))
        .isInstanceOf(RuntimeException.class)
        .hasMessage("broker unavailable");
  }
}
