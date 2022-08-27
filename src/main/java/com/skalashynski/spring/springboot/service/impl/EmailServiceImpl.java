package com.skalashynski.spring.springboot.service.impl;

import com.skalashynski.spring.springboot.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class EmailServiceImpl implements EmailService {
    public static final Logger LOGGER = LoggerFactory.getLogger(EmailServiceImpl.class);
    private final JavaMailSender mailSender;
    @Autowired
    @Qualifier("emailTemplate")
    private String emailTemplate;

    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    @Async // we don't want to block this client
    public void send(String to, String firstName, String link) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
        try {
            helper.setFrom("viktor.shilay@gmail.com");
            helper.setTo(to);
            helper.setSubject("Spring Boot. Confirm you email.");
            helper.setText(text(firstName, link), true);
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            LOGGER.warn("Failed to send logging");
            throw new IllegalStateException("failed tto send email", e);
        }
    }

    public String text(String name, String link) {
        return emailTemplate.replace("{{name}}", name).replace("{{link}}", link);
    }
}
