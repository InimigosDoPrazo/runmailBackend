package com.runmail.runmail.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Preferences {
    private String theme;
    private String language;
    private Notifications notifications;
}
