package com.microservice.creditcard.util;

import lombok.Getter;
import lombok.Setter;

/**
 * Esta clase contendrá los atributos que se enviarán
 * al microservicio de movements.
 * */
@Getter
@Setter
public class MovementDto {

  private Double amount;

  private String clientDocument;

  private String cardNumber;

  private String movementType;

}
