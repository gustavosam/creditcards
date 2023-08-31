package com.microservice.creditcard.service.mapper;


import com.microservice.creditcard.documents.CreditCardDocument;
import com.microservice.creditcard.model.Card;
import com.microservice.creditcard.model.CardRequest;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class CreditCardMappers {

    public static CreditCardDocument mapCardRequestToCard(CardRequest cardRequest){
        CreditCardDocument creditCardDocument = new CreditCardDocument();

        creditCardDocument.setCardAmount(cardRequest.getCardAmount());
        creditCardDocument.setCustomerDocument(cardRequest.getCustomerDocument());

        return creditCardDocument;
    }


    public static Card mapCreditCardDocumentToCard(CreditCardDocument creditCardDocument){

        Card card = new Card();
        card.setCardNumber(creditCardDocument.getCardNumber());
        card.setCardAmount(creditCardDocument.getCardAmount());
        card.setCustomerDocument(creditCardDocument.getCustomerDocument());
        card.setCardAmountAvailable(creditCardDocument.getCardAmountAvailable());
        card.setCardAmountConsumed(creditCardDocument.getCardAmountConsumed());
        card.setCreationDateCard(creditCardDocument.getCreationDateCard());
        return card;

    }
}
