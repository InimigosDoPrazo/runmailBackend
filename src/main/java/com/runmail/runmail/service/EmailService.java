package com.runmail.runmail.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.runmail.runmail.model.Email;
import com.runmail.runmail.repository.EmailRepository;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class EmailService {

    @Autowired
    private EmailRepository emailRepository;

    @Autowired
    private MongoTemplate mongoTemplate; // Usado para salvar diretamente na coleção 'spam'

    // Método para enviar emails com validação de spam
    public Email sendEmail(Email email) {
        validateEmail(email);

        // Bloqueia o envio se o email for identificado como spam
        if (isSpam(email)) {
            throw new ValidationException("Email identificado como spam e não foi enviado.");
        }

        return emailRepository.save(email); // Salva no repositório normal se não for spam
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

}


