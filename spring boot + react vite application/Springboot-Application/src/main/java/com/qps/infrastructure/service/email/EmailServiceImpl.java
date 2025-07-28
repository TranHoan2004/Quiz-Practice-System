package com.qps.infrastructure.service.email;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.charset.StandardCharsets;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EmailServiceImpl {
    JavaMailSender mailSender;

    @NonFinal
    @Value("${spring.mail.username}")
    String SENDER_EMAIL;

    public void sendSimpleMail(String to, String subject, String text) {
        var message = new SimpleMailMessage();

        message.setFrom(SENDER_EMAIL);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);

        mailSender.send(message);
    }

    public void sendHtmlMail(String to, String subject, String htmlText) throws Exception {
        var message = mailSender.createMimeMessage();
        var helper = new MimeMessageHelper(message, true, StandardCharsets.UTF_8.name());

        helper.setFrom(SENDER_EMAIL);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlText, true); // true = html

        mailSender.send(message);
    }

    public void sendEmailWithAttachment(String to, String subject, String htmlText, String path) throws Exception {
        var message = mailSender.createMimeMessage();
        var helper = new MimeMessageHelper(message, true, StandardCharsets.UTF_8.name());

        helper.setFrom(SENDER_EMAIL);
        helper.setTo(to);
        helper.setSubject(subject);

        var file = new FileSystemResource(new File(path));
        helper.addAttachment(file.getFilename(), file);

        mailSender.send(message);
    }
}