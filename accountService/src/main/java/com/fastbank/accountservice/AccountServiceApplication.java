package com.fastbank.accountservice;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Entry point for the FastBank Account Service Spring Boot application.
 *
 * <p>Bootstraps the application context and enables RabbitMQ listener support.
 */
@SpringBootApplication
@EnableRabbit
public class AccountServiceApplication {

  /**
   * Starts the FastBank Account Service.
   *
   * @param args command-line arguments passed to the application
   */
  public static void main(String[] args) {
    SpringApplication.run(AccountServiceApplication.class, args);
  }
}
