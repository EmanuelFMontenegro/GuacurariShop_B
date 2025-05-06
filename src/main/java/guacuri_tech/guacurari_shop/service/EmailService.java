package guacuri_tech.guacurari_shop.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;

    public void sendRecoveryEmail(String toEmail, String userName, String recoveryLink) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(toEmail);
        helper.setSubject("🔐 Recuperación de Contraseña - GuacurariTech");
        helper.setFrom("guacurari.empresa@gmail.com");

        // Thymeleaf para procesar el HTML
        Context context = new Context();
        context.setVariable("userName", userName);
        context.setVariable("recoveryLink", recoveryLink);

        String htmlContent = templateEngine.process("reset-password-email", context);
        helper.setText(htmlContent, true);

        mailSender.send(message);
    }
}
