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

    @GetMapping("/sent")
    public List<EmailResponseDTO> getEmailsSentByApp() {
        List<Email> emails = emailService.getEmailsSentByApp();
        return emails.stream().map(EmailService::convertToResponseDto).toList();
    }

    @GetMapping("/sent/by-sender")
    public List<EmailResponseDTO> getEmailsBySender(@RequestParam String sender) {
        return emailService.getEmailsBySender(sender);
    }

    @GetMapping("/mock/by-sender")
    public List<EmailResponseDTO> getMockedEmailsBySender(@RequestParam String sender) {
        return emailService.getMockedEmailsBySender(sender);
    }

    @GetMapping("/mock")
    public List<Email> getMockedEmails() {
        return emailService.getMockedEmails();
    }

    @GetMapping("/spam")
    public List<Email> getSpamEmails() {
        return emailService.getSpamEmails();
    }

    @PostMapping("/send")
    public EmailRequestDTO sendEmail(@Valid @RequestBody EmailRequestDTO emailRequestDTO) {
        Email email = emailService.sendEmail(EmailService.convertToEntity(emailRequestDTO));
        return EmailService.convertToRequestDto(email);
    }
}


