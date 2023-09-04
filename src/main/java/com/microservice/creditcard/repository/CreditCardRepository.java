package com.microservice.creditcard.repository;

import com.microservice.creditcard.documents.CreditCardDocument;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Esta interfaz contiene los métodos necesarios para el crud a la colección
 * CreditCard.
 * */
public interface CreditCardRepository extends MongoRepository<CreditCardDocument, String> {

  List<CreditCardDocument> findByClientDocument(String customerDocument);

}
