package com.microservice.creditcard.delegate;

import com.microservice.creditcard.api.CardApiDelegate;
import com.microservice.creditcard.model.Card;
import com.microservice.creditcard.model.CardConsume;
import com.microservice.creditcard.model.CardPayment;
import com.microservice.creditcard.model.CardRequest;
import com.microservice.creditcard.service.CreditCardService;
import com.microservice.creditcard.util.Constants;
import com.microservice.creditcard.util.ErrorC;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 *Esta clase implementa a los métodos generados por open api.
 * */
@Service
public class CreditCardDelegateImpl implements CardApiDelegate {

  @Autowired
  private CreditCardService cardService;

  @Override
  public Mono<ResponseEntity<Card>> createCreditCard(Mono<CardRequest> cardRequest,
                                                     ServerWebExchange exchange) {

    return cardRequest.flatMap(card -> {

      if(card.getClientDocument() == null){
        return Mono.just(ResponseEntity.badRequest().body(ErrorC.getInstance(Constants.DOCUMENT_EMPTY)));
      }

      if(card.getCardAmount() == null){
        return Mono.just(ResponseEntity.badRequest().body(ErrorC.getInstance(Constants.AMOUNT_CARD_EMPTY)));
      }

      return cardService.clientExist(card.getClientDocument()).flatMap(clientInf -> {

        if(clientInf.getDocument() == null){
          return Mono.just(ResponseEntity.badRequest().body(ErrorC.getInstance("PROBLEMS WITH CLIENT SERVICE")));
        }

        if(clientInf.getDocument().equalsIgnoreCase("NOT_EXIST")){
          return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorC.getInstance(Constants.CLIENT_NOT_EXIST)));
        }

        return cardService.createCard(card).map(ResponseEntity::ok);
      });

    });

    /*if (cardRequest.getClientDocument() == null) {
      return ResponseEntity.badRequest().body(ErrorC.getInstance(Constants.DOCUMENT_EMPTY));
    }

    if (cardRequest.getCardAmount() == null) {
      return ResponseEntity.badRequest().body(ErrorC.getInstance(Constants.AMOUNT_CARD_EMPTY));
    }

    return cardService.clientExist(cardRequest.getClientDocument())
      ? ResponseEntity.status(HttpStatus.CREATED).body(cardService.createCard(cardRequest))
      : ResponseEntity.badRequest().body(ErrorC.getInstance(Constants.CLIENT_NOT_EXIST));*/
  }

  @Override
  public Mono<ResponseEntity<Card>> consumeCreditCard(Mono<CardConsume> cardConsume,
                                                      ServerWebExchange exchange) {

    return cardConsume.flatMap(card -> {

      if(card.getCardNumber() == null){

        return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorC.getInstance(Constants.CARD_EMPTY)));
      }

      if(card.getCardConsumeAmount() == null || card.getCardConsumeAmount() == 0){

        return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorC.getInstance(Constants.AMOUNT_EMPTY)));
      }

      return cardService.cardExist(card.getCardNumber()).flatMap(value -> {

        if(!value){

          return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST)
                  .body(ErrorC.getInstance(Constants.CARD_NOT_EXIST)));
        }

        return cardService.amountAvailable(card.getCardNumber(), card.getCardConsumeAmount()).flatMap(available -> {

          if(!available){
            return Mono.just(ResponseEntity.badRequest().body(ErrorC.getInstance(Constants.NOT_MONEY)));
          }

          return cardService.cardConsume(card.getCardNumber(), card.getCardConsumeAmount()).map(ResponseEntity::ok);

        });

      });

    });

    /*if (card.getCardNumber() == null) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
              .body(ErrorC.getInstance(Constants.CARD_EMPTY));
    }

    if (card.getCardConsumeAmount() == null || card.getCardConsumeAmount() == 0) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
              .body(ErrorC.getInstance(Constants.AMOUNT_EMPTY));
    }

    if (! cardService.cardExist(card.getCardNumber())) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
              .body(ErrorC.getInstance(Constants.CARD_NOT_EXIST));
    }

    if (! cardService.amountAvailable(card.getCardNumber(), card.getCardConsumeAmount())) {
      return ResponseEntity.badRequest().body(ErrorC.getInstance(Constants.NOT_MONEY));
    }

    return ResponseEntity.status(HttpStatus.OK)
            .body(cardService.cardConsume(card.getCardNumber(), card.getCardConsumeAmount()));*/
  }

  @Override
  public Mono<ResponseEntity<Card>> payCreditCard(Mono<CardPayment> cardPayment,
                                                  ServerWebExchange exchange) {

    return cardPayment.flatMap(card -> {

      if(card.getCardNumber() == null){
        return Mono.just(ResponseEntity.badRequest().body(ErrorC.getInstance(Constants.CARD_EMPTY)));
      }

      if(card.getCardPaymentAmount() == null || card.getCardPaymentAmount() == 0){
        return Mono.just(ResponseEntity.badRequest().body(ErrorC.getInstance(Constants.AMOUNT_NOT_EMPTY)));
      }

      return cardService.cardExist(card.getCardNumber()).flatMap(exist -> {

        if(!exist){
          return Mono.just(ResponseEntity.badRequest().body(ErrorC.getInstance(Constants.CARD_NOT_EXIST)));
        }

        return cardService.validateIfYouCanPayCard(card.getCardNumber(), card.getCardPaymentAmount()).flatMap(value -> {

          if(!value){
            return Mono.just(ResponseEntity.badRequest().body(ErrorC.getInstance(Constants.OVERPAYMENT)));
          }
          return cardService.payCard(card.getCardNumber(), card.getCardPaymentAmount()).map(ResponseEntity::ok);
        });
      });
    });

    /*if (card.getCardNumber() == null) {
      return ResponseEntity.badRequest().body(ErrorC.getInstance(Constants.CARD_EMPTY));
    }

    if (card.getCardPaymentAmount() == null || card.getCardPaymentAmount() == 0) {
      return ResponseEntity.badRequest().body(ErrorC.getInstance(Constants.AMOUNT_NOT_EMPTY));
    }

    if (! cardService.cardExist(card.getCardNumber())) {
      return ResponseEntity.badRequest().body(ErrorC.getInstance(Constants.CARD_NOT_EXIST));
    }

    if (! cardService.validateIfYouCanPayCard(card.getCardNumber(), card.getCardPaymentAmount())) {
      return ResponseEntity.badRequest().body(ErrorC.getInstance(Constants.OVERPAYMENT));
    }

    return ResponseEntity.status(HttpStatus.OK)
            .body(cardService.payCard(card.getCardNumber(), card.getCardPaymentAmount()));*/
  }

  @Override
  public Mono<ResponseEntity<Flux<Card>>> getCreditCardsByClient(String clientDocument,
                                                                 ServerWebExchange exchange) {

    Flux<Card> cardFlux = cardService.getCardsByClient(clientDocument);

    return Mono.just(ResponseEntity.ok(cardFlux));

    //return ResponseEntity.status(HttpStatus.OK).body(cardService.getCardsByClient(clientDocument));
  }
}
