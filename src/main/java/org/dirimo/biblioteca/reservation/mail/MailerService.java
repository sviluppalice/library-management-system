package org.dirimo.biblioteca.reservation.mail;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailerService {
    private final JavaMailSender mailSender;

    public void sendMail(MailProperties mailProperties) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(mailProperties.getTo());
            helper.setSubject(mailProperties.getSubject());
            helper.setText(mailProperties.getBody(), true);
            mailSender.send(message);
        } catch (MessagingException e) {
            System.err.println("Errore nell'invio della mail: " + e.getMessage());
        }
    }
}
