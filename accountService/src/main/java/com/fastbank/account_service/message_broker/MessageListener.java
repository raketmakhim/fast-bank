package com.fastbank.account_service.message_broker;

import com.fastbank.account_service.dto.PersonRecord;
import com.fastbank.account_service.email.EmailService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MessageListener {

    @Autowired
    EmailService emailService;

    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
    public void receiveMessage(String message) throws JsonProcessingException {
        System.out.println("Received: " + message);

        ObjectMapper objectMapper = new ObjectMapper();
        PersonRecord person = objectMapper.readValue(message, PersonRecord.class);
        emailService.sendAccountCreatedEmail(person);
        // Process your message here
    }
}
