package com.redBus.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Component
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    public void sendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);

        javaMailSender.send(message);
    }

    public void sendEmailWithAttachment(String to, String subject, String body, byte[] attachmentData, String attachmentFileName) {
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body);
            helper.addAttachment(attachmentFileName, new ByteArrayResource(attachmentData));
            javaMailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace(); // Handle the exception as per your application's requirement
        }
    }

    public void sendCancellationEmailWithAttachment(String to, String subject, String body, byte[] attachmentData, String attachmentFileName) {
        sendEmailWithAttachment(to, subject, body, attachmentData, attachmentFileName);
    }
}
