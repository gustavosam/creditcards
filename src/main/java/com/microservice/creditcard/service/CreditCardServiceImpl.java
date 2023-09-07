package com.microservice.creditcard.service;

import com.microservice.creditcard.documents.CreditCardDocument;
import com.microservice.creditcard.feignclient.CustomerFeignClient;
import com.microservice.creditcard.feignclient.MovementFeignClient;
import com.microservice.creditcard.model.Card;
import com.microservice.creditcard.model.CardRequest;
import com.microservice.creditcard.repository.CreditCardRepository;
import com.microservice.creditcard.service.mapper.MapCard;
import com.microservice.creditcard.service.mapper.MapMovement;
import com.microservice.creditcard.util.CardDto;
import com.microservice.creditcard.util.ClientDto;
import com.microservice.creditcard.util.Constants;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Esta clase implementa los métodos de la interfaz CreditCardService
 * Contiene la lógica de negocio sobre las tarjetas de crédito.
 * */
@Service
public class CreditCardServiceImpl implements CreditCardService {

  @Autowired
  private CreditCardRepository creditCardRepository;

  @Autowired
  private CustomerFeignClient customerFeignClient;

  @Autowired
  private MovementFeignClient movementFeignClient;

  @Autowired
  private MapMovement mapMovement;

  @Autowired
  private MapCard mapCard;

  @Override
  public CardDto createCard(CardRequest cardRequest) {

    CreditCardDocument cardDoc = mapCard.mapCardRequestToCardDocument(cardRequest);
    cardDoc.setCreationDateCard(LocalDate.now());
    cardDoc.setAvailable(cardRequest.getCardAmount());
    cardDoc.setConsumed(0.0);

    CardDto cardNew = mapCard
            .mapCreditCardDocumentToCardDto(creditCardRepository.save(cardDoc));
    cardNew.setMessage(Constants.CARD_CREATED_OK);

    movementFeignClient.saveMovement(mapMovement.setValues(
        cardNew.getCardAmount(), cardNew.getClientDocument(),
        cardNew.getCardNumber(), Constants.CARD_CREATED
    ));

    return cardNew;
  }



  @Override
  public Boolean amountAvailable(String cardNumber, Double consume) {
    CreditCardDocument creditCardDocument = creditCardRepository.findById(cardNumber).get();

    return (creditCardDocument.getAvailable() - consume) >= 0;
  }



  @Override
  public Boolean cardExist(String cardNumber) {
    return creditCardRepository.existsById(cardNumber);
  }

  @Override
  public CardDto cardConsume(String cardNumber, Double consume) {
    CreditCardDocument creditCardDocument = creditCardRepository.findById(cardNumber).get();

    creditCardDocument.setConsumed(creditCardDocument.getConsumed() + consume);
    creditCardDocument.setAvailable(creditCardDocument.getAvailable() - consume);

    CardDto cardConsumed = mapCard
            .mapCreditCardDocumentToCardDto(creditCardRepository.save(creditCardDocument));
    cardConsumed.setMessage(Constants.CARD_CONSUMED_OK);

    movementFeignClient.saveMovement(mapMovement.setValues(
            consume, cardConsumed.getClientDocument(),
            cardConsumed.getCardNumber(), Constants.CARD_CONSUME
    ));

    return cardConsumed;
  }

  @Override
  public Boolean validateIfYouCanPayCard(String cardNumber, Double payment) {
    CreditCardDocument creditCardDocument = creditCardRepository.findById(cardNumber).get();

    return creditCardDocument.getConsumed() > 0
            && (payment > 0 && payment <= creditCardDocument.getConsumed());
  }

  @Override
  public CardDto payCard(String cardNumber, Double payment) {
    CreditCardDocument creditCardDocument = creditCardRepository.findById(cardNumber).get();

    creditCardDocument.setConsumed(creditCardDocument.getConsumed() - payment);
    creditCardDocument.setAvailable(creditCardDocument.getAvailable() + payment);

    CardDto cardPaid = mapCard
            .mapCreditCardDocumentToCardDto(creditCardRepository.save(creditCardDocument));
    cardPaid.setMessage(Constants.CARD_PAID_OK);

    movementFeignClient.saveMovement(mapMovement.setValues(
            payment, cardPaid.getClientDocument(),
            cardPaid.getCardNumber(), Constants.CARD_PAY
    ));

    return cardPaid;
  }

  @Override
  public List<Card> getCardsByClient(String customerDocument) {
    List<CreditCardDocument> creditCardDocuments = creditCardRepository
            .findByClientDocument(customerDocument);

    if (creditCardDocuments.isEmpty()) {
      return new ArrayList<>();
    }

    return creditCardDocuments.stream()
            .filter(Objects::nonNull)
            .map(creditCardDocument -> mapCard.mapCreditCardDocumentToCardDto(creditCardDocument))
            .collect(Collectors.toList());
  }


  @Override
  public Boolean clientExist(String clientDocument) {
    ClientDto complementary = customerFeignClient.getClient(clientDocument);

    return complementary.getDocument() != null;
  }
}
