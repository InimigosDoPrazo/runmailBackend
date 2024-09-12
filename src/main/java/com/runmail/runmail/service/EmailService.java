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
    private MongoTemplate mongoTemplate; // Usado para salvar diretamente na coleção 'spam'

    // Usado para rastrear emails enviados recentemente (últimos 10 minutos)
    private final Map<String, List<EmailSentRecord>> recentEmailRecords = new ConcurrentHashMap<>();

    // Classe para armazenar informações sobre emails enviados recentemente
    private static class EmailSentRecord {
        private final String bodyHash; // Hash do corpo do email para detectar duplicatas
        private final LocalDateTime timestamp; // Timestamp do envio

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

    // Método para enviar emails com validação de spam e controle de taxa
    public Email sendEmail(Email email) {
        validateEmail(email);

        // Bloqueia o envio se o email for identificado como spam
        if (isSpam(email)) {
            throw new ValidationException("Email identificado como spam e não foi enviado.");
        }

        // Verifica e atualiza o controle de taxa e mensagens repetidas
        if (!canSendEmail(email)) {
            throw new ValidationException("Limite de envio de emails atingido ou mensagem repetida detectada.");
        }

        // Define a data de envio atual
        email.setDate(new Date());

        return emailRepository.save(email); // Salva no repositório normal se não for spam
    }


    // Verifica se é possível enviar o email (limite de 3 emails a cada 10 minutos e bloqueia mensagens repetidas)
    private boolean canSendEmail(Email email) {
        String recipient = email.getSender();
        String bodyHash = String.valueOf(email.getBody().hashCode());
        LocalDateTime now = LocalDateTime.now(ZoneId.of("UTC"));

        recentEmailRecords.putIfAbsent(recipient, new ArrayList<>());
        List<EmailSentRecord> emailRecords = recentEmailRecords.get(recipient);

        // Remove registros antigos (mais de 10 minutos)
        emailRecords.removeIf(record -> ChronoUnit.MINUTES.between(record.getTimestamp(), now) > 10);

        // Verifica emails repetidos (mesmo corpo)
        boolean isDuplicate = emailRecords.stream().anyMatch(record -> record.getBodyHash().equals(bodyHash));
        if (isDuplicate) {
            System.out.println("Email duplicado detectado e bloqueado: " + email.getSubject());
            return false;
        }

        // Verifica o limite de 3 emails a cada 10 minutos
        if (emailRecords.size() >= 3) {
            System.out.println("Limite de envio para " + recipient + " atingido.");
            return false;
        }

        // Se não for repetido e estiver dentro do limite, adiciona o email ao registro
        emailRecords.add(new EmailSentRecord(bodyHash, now));
        return true;
    }

    // Método para retornar os emails enviados pelo aplicativo, sem validação de spam
    public List<Email> getEmailsSentByApp() {
        return emailRepository.findAll(); // Retorna todos os emails enviados
    }

    public List<Email> getSpamEmails() {
        return mongoTemplate.findAll(Email.class, "spam");  // Consulta diretamente na coleção 'spam'
    }

    // Gera mock e redireciona os spams para a coleção spam
    public List<Email> getMockedEmails() {
        List<Email> mockEmails = generateMockEmails();
        return filterSpam(mockEmails);
    }

    // Método para buscar emails mockados por sender (não spam)
    public List<EmailResponseDTO> getMockedEmailsBySender(String sender) {
        List<Email> mockEmails = getMockedEmails(); // Obtem emails mockados (já filtrados por não-spam)
        return mockEmails.stream()
                .filter(email -> email.getSender() != null && email.getSender().equals(sender))
                .map(EmailService::convertToResponseDto)
                .toList(); // Converter para EmailResponseDTO
    }

    // Gera emails mockados lendo de um arquivo JSON e redireciona os spams
    private List<Email> generateMockEmails() {
        List<Email> mockEmails = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();

        try {
            // Carrega o arquivo JSON de mock emails
            InputStream inputStream = getClass().getResourceAsStream("/mockEmails.json");
            List<Email> emailsFromFile = mapper.readValue(inputStream, new TypeReference<List<Email>>() {
            });

            // Valida e redireciona os spams
            for (Email email : emailsFromFile) {
                validateEmail(email);

                if (isSpam(email)) {
                    // Se for spam, salva na coleção de spam
                    mongoTemplate.save(email, "spam");
                    System.out.println("Email identificado como spam e salvo: " + email.getSubject());
                } else {
                    mockEmails.add(email);  // Adiciona à lista se não for spam
                }
            }
        } catch (Exception e) {
            System.out.println("Erro ao carregar e-mails mockados: " + e.getMessage());
        }

        return mockEmails;
    }

    // Método para buscar emails enviados por sender (remetente)
    public List<EmailResponseDTO> getEmailsBySender(String sender) {
        List<Email> emails = emailRepository.findBySender(sender);
        return emails.stream().map(EmailService::convertToResponseDto).toList(); // Converter para EmailResponseDTO
    }

    // Método para validar email
    private void validateEmail(Email email) {
        if (email.getSubject().isEmpty() || email.getSender().isEmpty() || email.getBody().isEmpty()) {
            throw new ValidationException("Subject, sender, and body cannot be empty.");
        }
    }

    // Método para verificar se um email é spam
    private boolean isSpam(Email email) {
        String bodyLower = email.getBody().toLowerCase();
        return bodyLower.contains("prêmio") || bodyLower.contains("ganhe") || bodyLower.contains("grátis");
    }

    // Método para filtrar emails não spam
    private List<Email> filterSpam(List<Email> emails) {
        return emails.stream().filter(email -> !isSpam(email)).toList();
    }

    // Método para converter EmailRequestDTO para Email (Entidade)
    public static Email convertToEntity(EmailRequestDTO emailRequestDTO) {
        return new Email(null, emailRequestDTO.subject(), emailRequestDTO.sender(), emailRequestDTO.body(), null, false, false);
    }

    // Método para converter Email (Entidade) para EmailRequestDTO
    public static EmailRequestDTO convertToRequestDto(Email email) {
        return new EmailRequestDTO(email.getSubject(), email.getSender(), email.getBody());
    }

    public static EmailResponseDTO convertToResponseDto(Email email) {
        return new EmailResponseDTO(email.getSubject(), email.getSender(), email.getBody(), email.getDate());
    }

}




