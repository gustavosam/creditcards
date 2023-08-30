package com.microservice.creditcard.repository;

import com.microservice.creditcard.documents.CreditCardDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CreditCardRepository extends MongoRepository<CreditCardDocument, String> {

    List<CreditCardDocument> findByCustomerDocument(String customerDocument);

}
