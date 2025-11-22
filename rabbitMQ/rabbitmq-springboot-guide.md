# Spring Boot RabbitMQ Integration Guide

## 1. Add Dependencies (both services)

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-amqp</artifactId>
</dependency>
```

## 2. Configure Connection (application.properties)

```properties
spring.rabbitmq.host=localhost
spring.rabbitmq.port=5672
spring.rabbitmq.username=admin
spring.rabbitmq.password=admin
```

If running in Docker, use `rabbitmq` as the host instead of `localhost`.

## 3. Service A - Sending Messages

### RabbitMQ Configuration
```java
@Configuration
public class RabbitMQConfig {
    
    @Bean
    public Queue myQueue() {
        return new Queue("my-queue", true);
    }
    
    @Bean
    public TopicExchange exchange() {
        return new TopicExchange("my-exchange");
    }
    
    @Bean
    public Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("my.routing.key");
    }
}
```

### Message Sender
```java
@Service
public class MessageSender {
    
    @Autowired
    private RabbitTemplate rabbitTemplate;
    
    public void sendMessage(String message) {
        rabbitTemplate.convertAndSend("my-exchange", "my.routing.key", message);
        System.out.println("Sent: " + message);
    }
}
```

### Example Controller
```java
@RestController
public class MessageController {
    
    @Autowired
    private MessageSender messageSender;
    
    @PostMapping("/send")
    public String send(@RequestBody String message) {
        messageSender.sendMessage(message);
        return "Message sent!";
    }
}
```

## 4. Service B - Receiving Messages

### RabbitMQ Configuration (same as Service A)
```java
@Configuration
public class RabbitMQConfig {
    
    @Bean
    public Queue myQueue() {
        return new Queue("my-queue", true);
    }
    
    @Bean
    public TopicExchange exchange() {
        return new TopicExchange("my-exchange");
    }
    
    @Bean
    public Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("my.routing.key");
    }
}
```

### Message Listener
```java
@Component
public class MessageListener {
    
    @RabbitListener(queues = "my-queue")
    public void receiveMessage(String message) {
        System.out.println("Received: " + message);
        // Process your message here
    }
}
```

## 5. Testing

1. Start RabbitMQ: `docker run -d --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:3-management`
2. Start both Spring Boot services
3. Send a message: `curl -X POST http://localhost:8080/send -d "Hello RabbitMQ!" -H "Content-Type: text/plain"`
4. Check Service B logs to see the received message
5. View queues at http://localhost:15672 (login: admin/admin)

## Bonus: Sending Objects

If you want to send JSON objects instead of strings:

```java
// Sender
public void sendOrder(Order order) {
    rabbitTemplate.convertAndSend("my-exchange", "my.routing.key", order);
}

// Receiver
@RabbitListener(queues = "my-queue")
public void receiveOrder(Order order) {
    System.out.println("Received order: " + order.getId());
}
```

Spring Boot will automatically serialize/deserialize objects as JSON!