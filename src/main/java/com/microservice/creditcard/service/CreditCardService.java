package com.microservice.creditcard.service;

import com.microservice.creditcard.model.Card;
import com.microservice.creditcard.model.CardRequest;
import com.microservice.creditcard.util.CardDto;
import com.microservice.creditcard.util.ClientDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * Interfaz que contiene los métodos que serán implementados para trabajar
 * la lógica de negocio.
 * */

public interface CreditCardService {

  Mono<CardDto> createCard(CardRequest cardRequest);

  Mono<ClientDto> clientExist(String clientDocument);

  Mono<Boolean> amountAvailable(String cardNumber, Double consume);

  Mono<Boolean> cardExist(String cardNumber);

  Mono<CardDto> cardConsume(String cardNumber, Double consume);

  Mono<Boolean> validateIfYouCanPayCard(String cardNumber, Double payment);

  Mono<CardDto> payCard(String cardNumber, Double payment);

  Flux<Card> getCardsByClient(String customerDocument);
}
