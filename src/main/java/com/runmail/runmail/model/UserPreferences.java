package com.runmail.runmail.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@Document(collection = "user_preferences")
public class UserPreferences {

    @Id
    private String userId;

    private Boolean theme;
    private Boolean autoSignature;
    private Boolean notificationsEnabled;
    private Boolean vibrationEnabled;
    private String signatureText;
    private String name;

}
