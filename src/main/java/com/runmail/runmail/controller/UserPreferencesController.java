package com.runmail.runmail.controller;

import com.runmail.runmail.model.UserPreferences;
import com.runmail.runmail.service.UserPreferencesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user/preferences")
public class UserPreferencesController {

    @Autowired
    private UserPreferencesService preferencesService;

    @PostMapping
    public UserPreferences savePreferences(@RequestBody UserPreferences preferences) {
        return preferencesService.savePreferences(preferences);
    }

    @PutMapping
    public UserPreferences updatePreferences(@RequestBody UserPreferences preferences) {
        return preferencesService.updatePreferences(preferences);
    }
}
