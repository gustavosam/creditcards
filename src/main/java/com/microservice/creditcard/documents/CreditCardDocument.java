package com.microservice.creditcard.documents;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Document(collection = "creditcards")
@Getter
@Setter
public class CreditCardDocument {

    @Id
    private String cardNumber;

    private Double cardAmount;

    private Double cardAmountConsumed;

    private Double cardAmountAvailable;

    private String customerDocument;

    private LocalDate creationDateCard;

}
