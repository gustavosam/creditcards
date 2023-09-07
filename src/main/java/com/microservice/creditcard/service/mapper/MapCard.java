package com.microservice.creditcard.service.mapper;

import com.microservice.creditcard.documents.CreditCardDocument;
import com.microservice.creditcard.model.CardRequest;
import com.microservice.creditcard.util.CardDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MapCard {

  CreditCardDocument mapCardRequestToCardDocument(CardRequest cardRequest);

  CardDto mapCreditCardDocumentToCardDto(CreditCardDocument creditCardDocument);
}
