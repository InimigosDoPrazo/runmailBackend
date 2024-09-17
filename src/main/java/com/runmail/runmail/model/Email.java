package com.runmail.runmail.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIgnoreType;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "emails")
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIgnoreType()
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Email {

    private String id;

    private String subject;

    private String sender;

    private String body;

    private Date date;

    private boolean isFavorite;

    private boolean isImportant;
}
