package com.runmail.runmail.service;

import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class SpamDetector {

    // Lista de palavras-chave de spam
    private static final List<String> SPAM_KEYWORDS = Arrays.asList("grátis", "dinheiro", "oferta");

    public boolean isSpam(String subject, String body) {
        // Verificar se o assunto ou corpo contém palavras-chave de spam
        return SPAM_KEYWORDS.stream().anyMatch(keyword ->
                subject.toLowerCase().contains(keyword) || body.toLowerCase().contains(keyword));
    }
}
