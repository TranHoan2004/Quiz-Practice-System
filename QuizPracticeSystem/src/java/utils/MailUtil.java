package utils;

import config.DotenvConfig;
import io.github.cdimascio.dotenv.Dotenv;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.Transport;

public class MailUtil {
    private static final String FROM_MAIL = DotenvConfig.get("EMAIL");
    private static final String PASSWORD = DotenvConfig.get("PASSWORD");
    private static final Logger logger = Logger.getLogger(MailUtil.class.getName());
    private static final String MAIL_SMTP_HOST = "smtp.gmail.com";
    private static final String MAIL_SMTP_PORT = "587";
    private static final boolean MAIL_SMTP_AUTH = true;
    private static final boolean MAIL_SMTP_STARTTLS_ENABLE = true;

    public static void sendMail(String toMail, String subject, String htmlContent) {
        logger.info("Sending mail to " + toMail);
        Properties props = new Properties();
        props.put("mail.smtp.host", MAIL_SMTP_HOST);
        props.put("mail.smtp.port", MAIL_SMTP_PORT);
        props.put("mail.smtp.auth", MAIL_SMTP_AUTH);
        props.put("mail.smtp.starttls.enable", MAIL_SMTP_STARTTLS_ENABLE);

        Authenticator auth = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(FROM_MAIL, PASSWORD);
            }
        };

        Session session = Session.getInstance(props, auth);

        try {
            Message message = new MimeMessage(session);
            message.setHeader("X-Mailer", "JavaMail");
            message.setFrom(new InternetAddress(FROM_MAIL));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(toMail));
            message.setSubject(subject);
            message.setContent(htmlContent, "text/html; charset=utf-8");

            Transport.send(message);
            logger.info("Sent message successfully....");
        } catch (MessagingException e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
    }
}
