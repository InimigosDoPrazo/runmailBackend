package com.runmail.runmail.controller;

import com.runmail.runmail.emailDTO.EmailRequestDTO;
import com.runmail.runmail.emailDTO.EmailResponseDTO;
import com.runmail.runmail.model.Email;
import com.runmail.runmail.service.EmailService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/runmail")
public class EmailController {

    @Autowired
    private EmailService emailService;

    // Endpoint para obter todos os emails enviados pelo app
    @GetMapping("/sent")
    public List<EmailResponseDTO> getEmailsSentByApp() {
        List<Email> emails = emailService.getEmailsSentByApp();
        return emails.stream().map(EmailService::convertToResponseDto).toList(); // Converter para EmailResponseDTO
    }

    // Endpoint para obter emails mockados, já filtrando os que não são spam
    @GetMapping("/mock")
    public List<Email> getMockedEmails() {
        return emailService.getMockedEmails();
    }

    // Endpoint para obter apenas os emails identificados como spam
    @GetMapping("/spam")
    public List<Email> getSpamEmails() {
        return emailService.getSpamEmails();
    }

    // Endpoint para enviar um email (com validação de spam)
    @PostMapping("/send")
    public EmailRequestDTO sendEmail(@Valid @RequestBody EmailRequestDTO emailRequestDTO) {
        Email email = emailService.sendEmail(EmailService.convertToEntity(emailRequestDTO)); // Converter DTO para entidade
        return EmailService.convertToRequestDto(email); // Converter para DTO após salvar
    }
}


