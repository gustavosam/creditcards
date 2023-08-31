package com.microservice.creditcard.repository;

import com.microservice.creditcard.documents.MovementsDocuments;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovementsRepository extends MongoRepository<MovementsDocuments, String> {
}
