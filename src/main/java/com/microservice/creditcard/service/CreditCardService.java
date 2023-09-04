package com.microservice.creditcard.service;

import com.microservice.creditcard.model.Card;
import com.microservice.creditcard.model.CardRequest;
import com.microservice.creditcard.util.CardDto;
import java.util.List;

/**
 * Interfaz que contiene los métodos que serán implementados para trabajar
 * la lógica de negocio.
 * */

public interface CreditCardService {

  CardDto createCard(CardRequest cardRequest);

  Boolean clientExist(String clientDocument);

  Boolean amountAvailable(String cardNumber, Double consume);

  Boolean cardExist(String cardNumber);

  CardDto cardConsume(String cardNumber, Double consume);

  Boolean validateIfYouCanPayCard(String cardNumber, Double payment);

  CardDto payCard(String cardNumber, Double payment);

  List<Card> getCardsByClient(String customerDocument);
}
