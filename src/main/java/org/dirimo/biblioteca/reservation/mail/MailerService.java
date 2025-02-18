package org.dirimo.biblioteca.reservation.mail;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.dirimo.biblioteca.reservation.Reservation;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailerService {
    private final JavaMailSender mailSender;

    public void sendMail(String to, String subject, String body) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, true);
            mailSender.send(message);
        } catch (MessagingException e) {
            System.err.println("Errore nell'invio della mail: " +e.getMessage());
        }
    }

    public String buildEmailContent(Reservation r) {
        return """
            <div style='font-family: Arial, sans-serif; padding: 15px; background-color: #f9f9f9; 
            border: 1px solid #ddd; border-radius: 8px; max-width: 600px;'>
                <h2 style='color: #2c3e50;'>📅 Your Reservation is Expiring Soon!</h2>
                <p>Dear <b>%s</b>,</p>
                <p>Your reservation <b>(ID: %d)</b> will expire on <b style='color: #e74c3c;'>%s</b>.</p>
                <p>Please return the book to the library, or contact us to make any necessary adjustments.</p>
                <hr style='border: 1px solid #ddd;'>
                <h3 style='color: #2980b9;'>📖 Book Details</h3>
                <ul>
                    <li><b>Title:</b> %s</li>
                    <li><b>Author:</b> %s</li>
                    <li><b>Publisher:</b> %s</li>
                    <li><b>Year:</b> %d</li>
                </ul>
                <p>Thank you,<br><i>The Library Team</i></p>
            </div>
            """.formatted(
                r.getCustomer(), r.getResId(), r.getResExpiryDate(),
                r.getBook().getTitle(), r.getBook().getAuthor(),
                r.getBook().getPublisher(), r.getBook().getYear()
        );
        }
}
