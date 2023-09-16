package com.microservice.creditcard.service;

import com.microservice.creditcard.documents.CreditCardDocument;
import com.microservice.creditcard.model.Card;
import com.microservice.creditcard.model.CardRequest;
import com.microservice.creditcard.repository.CreditCardRepository;
import com.microservice.creditcard.service.mapper.MapCard;
import com.microservice.creditcard.service.mapper.MapMovement;
import com.microservice.creditcard.util.CardDto;
import com.microservice.creditcard.util.ClientDto;
import com.microservice.creditcard.util.Constants;
import java.time.LocalDate;

import com.microservice.creditcard.webclient.ClientWebClient;
import com.microservice.creditcard.webclient.MovementWebClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Esta clase implementa los métodos de la interfaz CreditCardService
 * Contiene la lógica de negocio sobre las tarjetas de crédito.
 * */
@Service
public class CreditCardServiceImpl implements CreditCardService {

  @Autowired
  private CreditCardRepository creditCardRepository;

  //@Autowired
  //private CustomerFeignClient customerFeignClient;
  @Autowired
  private ClientWebClient clientWebClient;

  //@Autowired
  //private MovementFeignClient movementFeignClient;

  @Autowired
  private MovementWebClient movementWebClient;

  @Autowired
  private MapMovement mapMovement;

  @Autowired
  private MapCard mapCard;

  @Override
  public Mono<CardDto> createCard(CardRequest cardRequest) {

    CreditCardDocument cardDoc = mapCard.mapCardRequestToCardDocument(cardRequest);
    cardDoc.setCreationDateCard(LocalDate.now());
    cardDoc.setAvailable(cardRequest.getCardAmount());
    cardDoc.setConsumed(0.0);

    Mono<CreditCardDocument> creditCardDocumentMono = creditCardRepository.save(cardDoc);

    return creditCardDocumentMono.map(creditCardDocument -> {
      CardDto cardDto = mapCard.mapCreditCardDocumentToCardDto(creditCardDocument);
      cardDto.setMessage(Constants.CARD_CREATED_OK);

      movementWebClient.saveMovement(mapMovement.setValues(
              cardDto.getCardAmount(), cardDto.getClientDocument(),
              cardDto.getCardNumber(), Constants.CARD_CREATED
      )).subscribe();

      return cardDto;
    });

    /*CardDto cardNew = mapCard
            .mapCreditCardDocumentToCardDto(creditCardRepository.save(cardDoc));
    cardNew.setMessage(Constants.CARD_CREATED_OK);

    movementFeignClient.saveMovement(mapMovement.setValues(
        cardNew.getCardAmount(), cardNew.getClientDocument(),
        cardNew.getCardNumber(), Constants.CARD_CREATED
    ));

    return cardNew;*/
  }



  @Override
  public Mono<Boolean> amountAvailable(String cardNumber, Double consume) {

    Mono<CreditCardDocument> creditCardDocument = creditCardRepository.findById(cardNumber);

    return creditCardDocument.map(creditCard -> (creditCard.getAvailable() - consume) >= 0);

    //return (creditCardDocument.getAvailable() - consume) >= 0;
  }



  @Override
  public Mono<Boolean> cardExist(String cardNumber) {
    return creditCardRepository.existsById(cardNumber);
  }

  @Override
  public Mono<CardDto> cardConsume(String cardNumber, Double consume) {

    Mono<CreditCardDocument> creditCardDocument = creditCardRepository.findById(cardNumber);

    Mono<CreditCardDocument> creditCardDocumentUpdated = creditCardDocument.flatMap(card -> {

      card.setConsumed(card.getConsumed() + consume);
      card.setAvailable(card.getAvailable() - consume);

      return creditCardRepository.save(card);
    });

    return creditCardDocumentUpdated.map(creditCard -> {

      CardDto cardDto = mapCard.mapCreditCardDocumentToCardDto(creditCard);
      cardDto.setMessage(Constants.CARD_CONSUMED_OK);

      movementWebClient.saveMovement(mapMovement.setValues(
              consume, cardDto.getClientDocument(),
              cardDto.getCardNumber(), Constants.CARD_CONSUME
      )).subscribe();

      return cardDto;
    });


    /*creditCardDocument.setConsumed(creditCardDocument.getConsumed() + consume);
    creditCardDocument.setAvailable(creditCardDocument.getAvailable() - consume);

    CardDto cardConsumed = mapCard
            .mapCreditCardDocumentToCardDto(creditCardRepository.save(creditCardDocument));
    cardConsumed.setMessage(Constants.CARD_CONSUMED_OK);

    movementFeignClient.saveMovement(mapMovement.setValues(
            consume, cardConsumed.getClientDocument(),
            cardConsumed.getCardNumber(), Constants.CARD_CONSUME
    ));

    return cardConsumed;*/
  }

  @Override
  public Mono<Boolean> validateIfYouCanPayCard(String cardNumber, Double payment) {

    Mono<CreditCardDocument> creditCardDocument = creditCardRepository.findById(cardNumber);

    return creditCardDocument.map(creditCard -> (creditCard.getConsumed() > 0 && (payment > 0 && payment <= creditCard.getConsumed())));

    /*return creditCardDocument.getConsumed() > 0
            && (payment > 0 && payment <= creditCardDocument.getConsumed());*/
  }

  @Override
  public Mono<CardDto> payCard(String cardNumber, Double payment) {

    Mono<CreditCardDocument> creditCardDocument = creditCardRepository.findById(cardNumber);

    Mono<CreditCardDocument> creditCardDocumentUpdated = creditCardDocument.flatMap(creditCard -> {

      creditCard.setConsumed(creditCard.getConsumed() - payment);
      creditCard.setAvailable(creditCard.getAvailable() + payment);

      return creditCardRepository.save(creditCard);
    });

    return creditCardDocumentUpdated.map(cardDocument -> {

      CardDto cardDto = mapCard.mapCreditCardDocumentToCardDto(cardDocument);
      cardDto.setMessage(Constants.CARD_PAID_OK);

      movementWebClient.saveMovement(mapMovement.setValues(
              payment, cardDto.getClientDocument(),
              cardDto.getCardNumber(), Constants.CARD_PAY
      )).subscribe();

      return cardDto;
    });

    /*creditCardDocument.setConsumed(creditCardDocument.getConsumed() - payment);
    creditCardDocument.setAvailable(creditCardDocument.getAvailable() + payment);

    CardDto cardPaid = mapCard
            .mapCreditCardDocumentToCardDto(creditCardRepository.save(creditCardDocument));
    cardPaid.setMessage(Constants.CARD_PAID_OK);

    movementFeignClient.saveMovement(mapMovement.setValues(
            payment, cardPaid.getClientDocument(),
            cardPaid.getCardNumber(), Constants.CARD_PAY
    ));

    return cardPaid;*/
  }

  @Override
  public Flux<Card> getCardsByClient(String customerDocument) {

    Flux<CreditCardDocument> cardDocumentFlux = creditCardRepository
            .findByClientDocument(customerDocument);

    return cardDocumentFlux.map(cardDocument-> mapCard.mapCreditCardDocumentToCardDto(cardDocument));

    /*if (creditCardDocuments.isEmpty()) {
      return new ArrayList<>();
    }

    return creditCardDocuments.stream()
            .filter(Objects::nonNull)
            .map(creditCardDocument -> mapCard.mapCreditCardDocumentToCardDto(creditCardDocument))
            .collect(Collectors.toList());*/
  }


  @Override
  public Mono<ClientDto> clientExist(String clientDocument) {

    return clientWebClient.getClient(clientDocument);
  }
}
