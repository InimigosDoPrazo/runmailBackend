package com.runmail.runmail.repository;

import com.runmail.runmail.model.Email;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;


public interface EmailRepository extends MongoRepository<Email, String> {
    List<Email> findBySender(String sender);
}
