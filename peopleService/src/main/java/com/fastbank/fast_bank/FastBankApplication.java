package com.fastbank.fast_bank;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class FastBankApplication {

  public static void main(String[] args) {
    SpringApplication.run(FastBankApplication.class, args);
  }
}
