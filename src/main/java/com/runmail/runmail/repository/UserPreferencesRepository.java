package com.runmail.runmail.repository;

import com.runmail.runmail.model.UserPreferences;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserPreferencesRepository extends MongoRepository<UserPreferences, String> {
}
