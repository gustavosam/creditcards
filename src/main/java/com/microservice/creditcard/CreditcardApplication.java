package com.microservice.creditcard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Esta es la clase principal del proyecto.
 * */
@SpringBootApplication
//@EnableFeignClients
public class CreditcardApplication {

  public static void main(String[] args) {
    SpringApplication.run(CreditcardApplication.class, args);
  }

}
