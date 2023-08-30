package com.microservice.creditcard.service;

import com.microservice.creditcard.model.Card;
import com.microservice.creditcard.model.CardRequest;

import java.util.List;

public interface CreditCardService {

    Card createCard(CardRequest cardRequest);

    Boolean customerRequestExist(String customerDocument);

    Boolean validateCardAmountAvailable(String cardNumber, Double consume);

    Boolean validateIfCardExist(String cardNumber);

    Card cardConsume(String cardNumber, Double consume);

    Boolean validateIfYouCanPayCard(String cardNumber, Double consume);

    Card cardPayment(String cardNumber, Double payment);

    List<Card> getCardsByClient(String customerDocument);
}
