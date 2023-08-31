package com.microservice.creditcard.service;

import com.microservice.creditcard.documents.CreditCardDocument;
import com.microservice.creditcard.documents.CustomersComplementary;
import com.microservice.creditcard.documents.MovementsDocuments;
import com.microservice.creditcard.feignclient.CustomerFeignClient;
import com.microservice.creditcard.model.Card;
import com.microservice.creditcard.model.CardRequest;
import com.microservice.creditcard.repository.CreditCardRepository;
import com.microservice.creditcard.repository.MovementsRepository;
import com.microservice.creditcard.service.mapper.CreditCardMappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class CreditCardServiceImpl implements CreditCardService{

    @Autowired
    private CreditCardRepository creditCardRepository;

    @Autowired
    private CustomerFeignClient customerFeignClient;

    @Autowired
    private MovementsRepository movementsRepository;

    @Override
    public Card createCard(CardRequest cardRequest) {

        CreditCardDocument creditCardDocument = CreditCardMappers.mapCardRequestToCard(cardRequest);
        creditCardDocument.setCreationDateCard(LocalDate.now());
        creditCardDocument.setCardAmountAvailable(cardRequest.getCardAmount());
        creditCardDocument.setCardAmountConsumed(0.0);

        Card cardNew = CreditCardMappers.mapCreditCardDocumentToCard( creditCardRepository.save(creditCardDocument) );

        //LA ALTA DE TARJETA GENERA UN MOVIMIENTO
        MovementsDocuments movementCredit = new MovementsDocuments();
        movementCredit.setMovementType("ALTA TARJETA");
        movementCredit.setCustomerDocument(cardRequest.getCustomerDocument());
        movementCredit.setAmount(cardRequest.getCardAmount());
        movementCredit.setCardNumber(cardNew.getCardNumber());
        movementCredit.setMovementDate(LocalDate.now());

        movementsRepository.save(movementCredit);

        return cardNew;
    }



    @Override
    public Boolean validateCardAmountAvailable(String cardNumber, Double consume) {
        CreditCardDocument creditCardDocument = creditCardRepository.findById(cardNumber).get();

        return (creditCardDocument.getCardAmountAvailable() - consume) >= 0;
    }



    @Override
    public Boolean validateIfCardExist(String cardNumber) {
        return creditCardRepository.existsById(cardNumber);
    }

    @Override
    public Card cardConsume(String cardNumber, Double consume) {
        CreditCardDocument creditCardDocument = creditCardRepository.findById(cardNumber).get();

        creditCardDocument.setCardAmountConsumed( creditCardDocument.getCardAmountConsumed() + consume );
        creditCardDocument.setCardAmountAvailable( creditCardDocument.getCardAmountAvailable() - consume );

        Card cardConsumed = CreditCardMappers.mapCreditCardDocumentToCard(creditCardRepository.save(creditCardDocument));

        //EL CONSUMO DE TARJETA PRODUCE UN MOVIMIENTO
        MovementsDocuments movementCredit = new MovementsDocuments();
        movementCredit.setMovementType("CONSUMO TARJETA");
        movementCredit.setCustomerDocument(creditCardDocument.getCustomerDocument());
        movementCredit.setAmount(consume);
        movementCredit.setCardNumber(cardNumber);
        movementCredit.setMovementDate(LocalDate.now());

        movementsRepository.save(movementCredit);

        return cardConsumed;
    }

    @Override
    public Boolean validateIfYouCanPayCard(String cardNumber, Double payment) {
        CreditCardDocument creditCardDocument = creditCardRepository.findById(cardNumber).get();

        return creditCardDocument.getCardAmountConsumed() > 0 && (payment > 0 && payment <= creditCardDocument.getCardAmountConsumed());
    }

    @Override
    public Card cardPayment(String cardNumber, Double payment) {
        CreditCardDocument creditCardDocument = creditCardRepository.findById(cardNumber).get();

        creditCardDocument.setCardAmountConsumed( creditCardDocument.getCardAmountConsumed() - payment );
        creditCardDocument.setCardAmountAvailable( creditCardDocument.getCardAmountAvailable() + payment);

        Card cardPaid = CreditCardMappers.mapCreditCardDocumentToCard(creditCardRepository.save(creditCardDocument));

        //EL PAGO DE TARJETA PRODUCE UN MOVIMIENTO
        MovementsDocuments movementCredit = new MovementsDocuments();
        movementCredit.setMovementType("PAGO TARJETA");
        movementCredit.setCustomerDocument(creditCardDocument.getCustomerDocument());
        movementCredit.setAmount(payment);
        movementCredit.setCardNumber(cardNumber);
        movementCredit.setMovementDate(LocalDate.now());

        movementsRepository.save(movementCredit);

        return cardPaid;
    }

    @Override
    public List<Card> getCardsByClient(String customerDocument) {
        List<CreditCardDocument> creditCardDocuments = creditCardRepository.findByCustomerDocument(customerDocument);

        if(creditCardDocuments.isEmpty()){
            return new ArrayList<>();
        }

        return creditCardDocuments.stream().filter(Objects::nonNull).map(CreditCardMappers::mapCreditCardDocumentToCard).collect(Collectors.toList());
    }


    @Override
    public Boolean customerRequestExist(String customerDocument) {
        CustomersComplementary complementary = customerFeignClient.getCustomerById(customerDocument);

        return complementary.getCustomerDocument() != null;
    }
}
