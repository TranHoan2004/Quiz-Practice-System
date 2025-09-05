package com.qps.infrastructure.service.email;

import jakarta.mail.MessagingException;

public interface EmailService {
    void sendNormalEmail(String to, String subject, String body) throws MessagingException;

    void sendEmailAttachFile(String to, String subject, String body, String path) throws MessagingException;
}
