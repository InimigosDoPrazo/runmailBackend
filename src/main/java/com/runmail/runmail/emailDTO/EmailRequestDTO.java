package com.runmail.runmail.emailDTO;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record EmailRequestDTO(

        @NotEmpty(message = "O Assunto não pode ser vazio!")
        @Size(min = 3, max = 60, message = "O Assunto deve conter no mínimo 03 e no máximo 60 caracteres.")
        String subject,

        @jakarta.validation.constraints.Email(message = "Formato de e-mail do destinatario inválido!")
        @NotEmpty(message = "O Destinatário não pode ser vazio!")
        @Size(min = 3, max = 50, message = "O Destinatário deve conter no mínimo 03 e no máximo 50 caracteres.")
        String sender,

        @NotEmpty(message = "Você precisa inserir uma mensagem!")
        @Size(min = 3, max = 10000, message = "A mensagem deve conter no mínimo 03 e no máximo 10.000 caracteres.")
        String body

) {
}
