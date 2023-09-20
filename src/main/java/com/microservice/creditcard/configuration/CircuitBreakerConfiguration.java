package com.microservice.creditcard.configuration;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class CircuitBreakerConfiguration {

  @Bean
  public CircuitBreakerRegistry circuitBreakerRegistry() {
    CircuitBreakerConfig config = CircuitBreakerConfig.custom()
            .failureRateThreshold(1) // Umbral de tasa de fallo al 1% o cualquier valor menor
            .waitDurationInOpenState(Duration.ofMillis(10000)) // Tiempo de espera en estado abierto
            .build();

    return CircuitBreakerRegistry.of(config);
  }
}