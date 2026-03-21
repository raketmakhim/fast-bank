package com.fastbank.fast_bank;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Entry point for the FastBank People Service Spring Boot application.
 *
 * <p>Bootstraps the application context and enables Feign clients for inter-service communication.
 */
@SpringBootApplication
@EnableFeignClients
public class FastBankApplication {

  /**
   * Starts the FastBank People Service.
   *
   * @param args command-line arguments passed to the application
   */
  public static void main(String[] args) {
    SpringApplication.run(FastBankApplication.class, args);
  }
}
