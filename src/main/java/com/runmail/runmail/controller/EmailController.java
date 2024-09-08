package com.runmail.runmail.controller;

import com.runmail.runmail.model.Email;
import com.runmail.runmail.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/emails")
public class EmailController {

    @Autowired
    private EmailService emailService;

    @GetMapping("/listaDeEmailsMock")
    public List<Email> getAllEmailsMock() {
        return emailService.getAllEmailsMock();
    }

    @GetMapping("mock/{id}")
    public Email getEmailMockById(@PathVariable String id) {
        return emailService.getEmailMockById(id);
    }

    @GetMapping("/Enviados")
    public List<Email> getAllEmails() {
        return emailService.getAllEmails();
    }

    @GetMapping("/Enviados/{id}")
    public Email getEmailById(@PathVariable String id) {
        return emailService.getEmailById(id);
    }

    @PostMapping("/enviar")
    public Email sendEmail(@RequestBody Email email) {
        return emailService.sendEmail(email);
    }
}

