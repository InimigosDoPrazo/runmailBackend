package com.runmail.runmail.repository;

import com.runmail.runmail.model.Preference;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PreferenceRepository extends MongoRepository<Preference, String> {
    Preference findByUserId(String userId);
}
