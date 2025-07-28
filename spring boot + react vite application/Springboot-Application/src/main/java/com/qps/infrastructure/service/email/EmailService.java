package com.qps.infrastructure.service.email;

public interface EmailService {
    void sendNormalEmail(String to, String subject, String body);

    void sendEmailAttachFile(String to, String subject, String body, String path);
}
