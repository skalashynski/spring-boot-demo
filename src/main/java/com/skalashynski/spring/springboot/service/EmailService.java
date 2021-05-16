package com.skalashynski.spring.springboot.service;

public interface EmailService {
    void send(String to, String email, String link);
}
