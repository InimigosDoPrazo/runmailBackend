package com.runmail.runmail.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@Document(collection = "user_preferences")
public class UserPreferences {
    @Id
    private String userId;
    private String theme;
    private String language;
    private Notifications notifications;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Notifications {
        private boolean email;
        private boolean sms;
    }
}
