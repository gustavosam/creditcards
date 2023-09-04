package com.microservice.creditcard.documents;

import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Esta clase es la representación de la colección creditcards en mongo db.
 * */
@Document(collection = "creditcards")
@Getter
@Setter
public class CreditCardDocument {

  @Id
  private String cardNumber;

  private Double cardAmount;

  private Double consumed;

  private Double available;

  private String clientDocument;

  private LocalDate creationDateCard;

}
