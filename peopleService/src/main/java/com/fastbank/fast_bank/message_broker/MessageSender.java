package com.fastbank.fast_bank.message_broker;

import com.fastbank.fast_bank.dto.PersonRequest;
import com.fastbank.fast_bank.entity.Person;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    public void sendMessage(PersonRequest person) throws JsonProcessingException {
        String personJson = objectMapper.writeValueAsString(person);

        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.ROUTING_KEY, person);
        System.out.println("Sent: " + person);
    }
}
