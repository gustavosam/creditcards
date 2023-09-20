package com.microservice.creditcard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Esta es la clase principal del proyecto.
 * */
@EnableDiscoveryClient
@SpringBootApplication
//@EnableFeignClients
public class CreditcardApplication {

  public static void main(String[] args) {
    SpringApplication.run(CreditcardApplication.class, args);
  }

}
