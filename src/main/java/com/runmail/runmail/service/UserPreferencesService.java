package com.runmail.runmail.service;

import com.runmail.runmail.model.UserPreferences;
import com.runmail.runmail.repository.UserPreferencesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserPreferencesService {

    @Autowired
    private UserPreferencesRepository preferencesRepository;

    public UserPreferences savePreferences(UserPreferences preferences) {
        return preferencesRepository.save(preferences);
    }

    public UserPreferences updatePreferences(UserPreferences preferences) {
        return preferencesRepository.save(preferences);
    }

    public List<UserPreferences> getPreferences() {
        return preferencesRepository.findAll();
    }
}
