package com.runmail.runmail.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.runmail.runmail.emailDTO.EmailRequestDTO;
import com.runmail.runmail.emailDTO.EmailResponseDTO;
import com.runmail.runmail.model.Email;
import com.runmail.runmail.repository.EmailRepository;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class EmailService {

    @Autowired
    private EmailRepository emailRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    private final Map<String, List<EmailSentRecord>> recentEmailRecords = new ConcurrentHashMap<>();

    private static class EmailSentRecord {
        private final String bodyHash;
        private final LocalDateTime timestamp;

        public EmailSentRecord(String bodyHash, LocalDateTime timestamp) {
            this.bodyHash = bodyHash;
            this.timestamp = timestamp;
        }

        public String getBodyHash() {
            return bodyHash;
        }

        public LocalDateTime getTimestamp() {
            return timestamp;
        }
    }

    public Email sendEmail(Email email) {
        validateEmail(email);


        if (isSpam(email)) {
            throw new ValidationException("Email identificado como spam e não foi enviado.");
        }


        if (!canSendEmail(email)) {
            throw new ValidationException("Limite de envio de emails atingido ou mensagem repetida detectada.");
        }

        email.setDate(new Date());

        return emailRepository.save(email);
    }


    private boolean canSendEmail(Email email) {
        String recipient = email.getSender();
        String bodyHash = String.valueOf(email.getBody().hashCode());
        LocalDateTime now = LocalDateTime.now(ZoneId.of("UTC"));

        recentEmailRecords.putIfAbsent(recipient, new ArrayList<>());
        List<EmailSentRecord> emailRecords = recentEmailRecords.get(recipient);

        emailRecords.removeIf(record -> ChronoUnit.MINUTES.between(record.getTimestamp(), now) > 10);

        boolean isDuplicate = emailRecords.stream().anyMatch(record -> record.getBodyHash().equals(bodyHash));
        if (isDuplicate) {
            System.out.println("Email duplicado detectado e bloqueado: " + email.getSubject());
            return false;
        }
        if (emailRecords.size() >= 3) {
            System.out.println("Limite de envio para " + recipient + " atingido.");
            return false;
        }
        emailRecords.add(new EmailSentRecord(bodyHash, now));
        return true;
    }

    public List<Email> getEmailsSentByApp() {
        return emailRepository.findAll();
    }

    public List<Email> getSpamEmails() {
        return mongoTemplate.findAll(Email.class, "spam");
    }

    public List<Email> getMockedEmails() {
        List<Email> mockEmails = generateMockEmails();
        return filterSpam(mockEmails);
    }

    public List<EmailResponseDTO> getMockedEmailsBySender(String sender) {
        List<Email> mockEmails = getMockedEmails();
        return mockEmails.stream()
                .filter(email -> email.getSender() != null && email.getSender().equals(sender))
                .map(EmailService::convertToResponseDto)
                .toList();
    }

    private List<Email> generateMockEmails() {
        List<Email> mockEmails = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();

        try {
            InputStream inputStream = getClass().getResourceAsStream("/mockEmails.json");
            List<Email> emailsFromFile = mapper.readValue(inputStream, new TypeReference<List<Email>>() {
            });

            for (Email email : emailsFromFile) {
                validateEmail(email);
                if (isSpam(email)) {
                    mongoTemplate.save(email, "spam");
                    System.out.println("Email identificado como spam e salvo: " + email.getSubject());
                } else {
                    mockEmails.add(email);
                }
            }
        } catch (Exception e) {
            System.out.println("Erro ao carregar e-mails mockados: " + e.getMessage());
        }

        return mockEmails;
    }

    public List<EmailResponseDTO> getEmailsBySender(String sender) {
        List<Email> emails = emailRepository.findBySender(sender);
        return emails.stream().map(EmailService::convertToResponseDto).toList(); // Converter para EmailResponseDTO
    }

    private void validateEmail(Email email) {
        if (email.getSubject().isEmpty() || email.getSender().isEmpty() || email.getBody().isEmpty()) {
            throw new ValidationException("Subject, sender, and body cannot be empty.");
        }
    }

    private boolean isSpam(Email email) {
        String bodyLower = email.getBody().toLowerCase();
        return bodyLower.contains("prêmio") || bodyLower.contains("ganhe") || bodyLower.contains("grátis");
    }

    private List<Email> filterSpam(List<Email> emails) {
        return emails.stream().filter(email -> !isSpam(email)).toList();
    }

    public static Email convertToEntity(EmailRequestDTO emailRequestDTO) {
        return new Email(null, emailRequestDTO.subject(), emailRequestDTO.sender(), emailRequestDTO.body(), null, false, false);
    }

    public static EmailRequestDTO convertToRequestDto(Email email) {
        return new EmailRequestDTO(email.getSubject(), email.getSender(), email.getBody());
    }

    public static EmailResponseDTO convertToResponseDto(Email email) {
        return new EmailResponseDTO(email.getSubject(), email.getSender(), email.getBody(), email.getDate());
    }

}




