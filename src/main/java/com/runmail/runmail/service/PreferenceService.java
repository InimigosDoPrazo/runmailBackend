package com.runmail.runmail.service;

import com.runmail.runmail.model.Preference;
import com.runmail.runmail.repository.PreferenceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PreferenceService {

    @Autowired
    private PreferenceRepository preferenceRepository;

    public Preference getPreferences(String userId) {
        return preferenceRepository.findByUserId(userId);
    }

    public Preference updatePreferences(String userId, Preference preferences) {
        Preference existingPreference = preferenceRepository.findByUserId(userId);
        if (existingPreference != null) {
            existingPreference.setTheme(preferences.getTheme());
            existingPreference.setColor(preferences.getColor());
            return preferenceRepository.save(existingPreference);
        }
        return null;
    }
}
