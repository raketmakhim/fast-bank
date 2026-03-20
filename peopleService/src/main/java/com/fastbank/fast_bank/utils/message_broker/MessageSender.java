package com.fastbank.fast_bank.utils.message_broker;

import com.fastbank.fast_bank.model.entity.Person;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MessageSender {

    @Autowired private RabbitTemplate rabbitTemplate;

    public void sendMessage(Person person) {
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.ROUTING_KEY, person);
        log.info("Sent: {}", person);
    }
}
