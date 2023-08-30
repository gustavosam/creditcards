package com.microservice.creditcard.service;

import com.microservice.creditcard.documents.CreditCardDocument;
import com.microservice.creditcard.model.Card;
import com.microservice.creditcard.model.CardRequest;
import com.microservice.creditcard.repository.CreditCardRepository;
import com.microservice.creditcard.repository.CustomerRepository;
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
    private CustomerRepository customerRepository;

    @Override
    public Card createCard(CardRequest cardRequest) {

        CreditCardDocument creditCardDocument = CreditCardMappers.mapCardRequestToCard(cardRequest);
        creditCardDocument.setCreationDateCard(LocalDate.now());
        creditCardDocument.setCardAmountAvailable(cardRequest.getCardAmount());
        creditCardDocument.setCardAmountConsumed(0.0);

        return CreditCardMappers.mapCreditCardDocumentToCard( creditCardRepository.save(creditCardDocument) );
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

        return CreditCardMappers.mapCreditCardDocumentToCard(creditCardRepository.save(creditCardDocument));
    }

    @Override
    public Boolean validateIfYouCanPayCard(String cardNumber, Double consume) {
        CreditCardDocument creditCardDocument = creditCardRepository.findById(cardNumber).get();

        return creditCardDocument.getCardAmountConsumed() > 0 && (consume > 0 && consume <= creditCardDocument.getCardAmountConsumed());
    }

    @Override
    public Card cardPayment(String cardNumber, Double payment) {
        CreditCardDocument creditCardDocument = creditCardRepository.findById(cardNumber).get();

        creditCardDocument.setCardAmountConsumed( creditCardDocument.getCardAmountConsumed() - payment );
        creditCardDocument.setCardAmountAvailable( creditCardDocument.getCardAmountAvailable() + payment);

        return CreditCardMappers.mapCreditCardDocumentToCard(creditCardRepository.save(creditCardDocument));
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
        return customerRepository.existsById(customerDocument);
    }
}
