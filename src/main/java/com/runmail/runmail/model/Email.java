package com.runmail.runmail.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIgnoreType;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
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
    @Id
    private String id;

    @NotEmpty(message = "O Assunto não pode ser vazio!")
    private String subject;

    @jakarta.validation.constraints.Email(message = "Formato de e-mail inválido!")
    @NotEmpty(message = "O Destinatário não pode ser vazio!")
    private String sender;

    @NotEmpty(message = "Você precisa inserir uma mensagem!")
    private String body;

    private Date date;

    private boolean isFavorite;

    private boolean isImportant;
}
