package com.microservice.creditcard.repository;

import com.microservice.creditcard.documents.CreditCardDocument;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

/**
 * Esta interfaz contiene los métodos necesarios para el crud a la colección
 * CreditCard.
 * */
public interface CreditCardRepository extends ReactiveMongoRepository<CreditCardDocument, String> {

  Flux<CreditCardDocument> findByClientDocument(String customerDocument);

}
