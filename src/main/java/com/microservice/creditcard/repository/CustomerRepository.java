package com.microservice.creditcard.repository;

import com.microservice.creditcard.documents.CustomerDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CustomerRepository extends MongoRepository<CustomerDocument, String> {
}
