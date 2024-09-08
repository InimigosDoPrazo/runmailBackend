package com.runmail.runmail.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "preferences")
public class Preference {
    @Id
    private String id;
    private String userId;
    private String theme;
    private String color;
}
