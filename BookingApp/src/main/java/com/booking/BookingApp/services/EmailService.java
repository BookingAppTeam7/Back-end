package com.booking.BookingApp.services;

import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private MailProperties mailProperties;

    public void send(String to, String body, String subject) throws MessagingException {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

            helper.setFrom(mailProperties.getUsername());
            helper.setTo(to);
            helper.setText(body, true);
            helper.setSubject(subject);

            mailSender.send(mimeMessage);
            System.out.println("Mail was sent successfully");
        }catch (Exception e){
            System.out.println("Exception: " + e.toString());
        }
    }
}
