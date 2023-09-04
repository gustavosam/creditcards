package com.microservice.creditcard.service.mapper;

import com.microservice.creditcard.documents.CreditCardDocument;
import com.microservice.creditcard.model.CardRequest;
import com.microservice.creditcard.util.CardDto;

/**
 * Esta clase nos permite convertir una clase en otra.
 * */
public class CreditCardMappers {

  /**
   * Este método permite convertir una clase CardRequest a una clase CreditCardDocument.
   * */
  public static CreditCardDocument mapCardRequestToCardDocument(CardRequest cardRequest) {
    CreditCardDocument creditCardDocument = new CreditCardDocument();

    creditCardDocument.setCardAmount(cardRequest.getCardAmount());
    creditCardDocument.setClientDocument(cardRequest.getClientDocument());

    return creditCardDocument;
  }

  /**
   * Este método permite convertir una clase CreditCardDocument en CardDto.
   * */
  public static CardDto mapCreditCardDocumentToCardDto(CreditCardDocument creditCardDocument) {
    CardDto card = new CardDto();

    card.setCardNumber(creditCardDocument.getCardNumber());
    card.setCardAmount(creditCardDocument.getCardAmount());
    card.setClientDocument(creditCardDocument.getClientDocument());
    card.setAvailable(creditCardDocument.getAvailable());
    card.setConsumed(creditCardDocument.getConsumed());
    card.setCreationDateCard(creditCardDocument.getCreationDateCard());

    return card;
  }
}
