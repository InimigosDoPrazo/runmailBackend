package com.runmail.runmail.repository;

import com.runmail.runmail.model.Email;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface EmailRepository extends MongoRepository<Email, String> {
}
