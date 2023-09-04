package com.microservice.creditcard.util;

import com.microservice.creditcard.model.Card;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

/**
 * Esta clase extiende de Card
 * Se le añade otros atributos que se mostrarán al cliente durante la petición.
 * */
@Getter
@Setter
public class CardDto extends Card {

  private String cardNumber;

  private Double cardAmount;

  private Double consumed;

  private Double available;

  private String clientDocument;

  private LocalDate creationDateCard;
}
