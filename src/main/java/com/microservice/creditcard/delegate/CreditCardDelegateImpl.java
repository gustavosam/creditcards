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

/**
 *Esta clase implementa a los métodos generados por open api.
 * */
@Service
public class CreditCardDelegateImpl implements CardApiDelegate {

  @Autowired
  private CreditCardService cardService;

  @Override
  public ResponseEntity<Card> createCreditCard(CardRequest cardRequest) {

    if (cardRequest.getClientDocument() == null) {
      return ResponseEntity.badRequest().body(ErrorC.getInstance(Constants.DOCUMENT_EMPTY));
    }

    if (cardRequest.getCardAmount() == null) {
      return ResponseEntity.badRequest().body(ErrorC.getInstance(Constants.AMOUNT_CARD_EMPTY));
    }

    return cardService.clientExist(cardRequest.getClientDocument())
      ? ResponseEntity.status(HttpStatus.CREATED).body(cardService.createCard(cardRequest))
      : ResponseEntity.badRequest().body(ErrorC.getInstance(Constants.CLIENT_NOT_EXIST));
  }

  @Override
  public ResponseEntity<Card> consumeCreditCard(CardConsume card) {

    if (card.getCardNumber() == null) {
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
            .body(cardService.cardConsume(card.getCardNumber(), card.getCardConsumeAmount()));
  }

  @Override
  public ResponseEntity<Card> payCreditCard(CardPayment card) {

    if (card.getCardNumber() == null) {
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
            .body(cardService.payCard(card.getCardNumber(), card.getCardPaymentAmount()));
  }

  @Override
  public ResponseEntity<List<Card>> getCreditCardsByClient(String clientDocument) {
    return ResponseEntity.status(HttpStatus.OK).body(cardService.getCardsByClient(clientDocument));
  }
}
