package com.runmail.runmail.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDate;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "emails")
public class Email {
    @Id
    private String id; // Usando String como ID no MongoDB
    private String subject;
    private String sender;
    private String body;
    private LocalDate date;
    private boolean isFavorite;
    private boolean isImportant;
}
