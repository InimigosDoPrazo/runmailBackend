package com.runmail.runmail.service;

import com.runmail.runmail.model.Email;
import com.runmail.runmail.repository.EmailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Service
public class EmailService {

    @Autowired
    private EmailRepository emailRepository;


    public List<Email> getAllEmails() {
        return emailRepository.findAll();
    }

    public Email getEmailById(String id) {
        return emailRepository.findById(id).orElse(null);
    }

    public Email sendEmail(Email email) {
        return emailRepository.save(email);
    }

    // Lista de emails mockada
    private List<Email> mockEmails = Arrays.asList(
            new Email("1", "Assunto ", "remetente1@example.com", "Corpo do email 1", LocalDate.now(), false, true),
            new Email("2", "Assunto ", "remetente2@example.com", "Corpo do email 2", LocalDate.now().minusDays(1), true, false),
            new Email("3", "Assunto ", "remetente3@example.com", "Corpo do email 3", LocalDate.now().minusDays(2), false, false),
            new Email("4", "Assunto ", "remetente4@example.com", "Corpo do email 4", LocalDate.now().minusDays(3), true, true),
            new Email("5", "Assunto ", "remetente5@example.com", "Corpo do email 5", LocalDate.now(), false, true),
            new Email("6", "Assunto ", "remetente6@example.com", "Corpo do email 6", LocalDate.now().minusDays(1), true, false),
            new Email("7", "Assunto ", "remetente7@example.com", "Corpo do email 7", LocalDate.now().minusDays(2), false, false),
            new Email("8", "Assunto ", "remetente8@example.com", "Corpo do email 8", LocalDate.now().minusDays(3), true, true),
            new Email("9", "Assunto ", "remetente9@example.com", "Corpo do email 9", LocalDate.now().minusDays(2), false, false),
            new Email("10", "Assunto ", "remetente10@example.com", "Corpo do email 10", LocalDate.now().minusDays(3), true, true)
    );

    public List<Email> getAllEmailsMock() {
        // Aqui estamos retornando a lista mockada em vez de consultar o banco de dados
        return mockEmails;
    }

    public Email getEmailMockById(String id) {
        // Simulando a busca na lista mockada
        return mockEmails.stream()
                .filter(email -> email.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

}
