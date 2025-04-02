package guacuri_tech.guacurari_shop.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendRecoveryEmail(String toEmail, String userName, String recoveryLink) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(toEmail);
        helper.setSubject("Recuperación de Contraseña - GuacurariTech");
        helper.setFrom("guacurari.empresa@gmail.com");  // Usa un email con tu dominio
        helper.setText(getHtmlContent(userName, recoveryLink), true);  // Set HTML content

        mailSender.send(message);
    }

    private String getHtmlContent(String userName, String recoveryLink) {
        return "<!DOCTYPE html>" +
                "<html><head><meta charset='UTF-8'></head><body style='font-family: Arial, sans-serif;'>" +
                "<p style='font-size: 16px; color: #333;'>Hola <b>" + userName + "</b>,</p>" +
                "<p style='font-size: 14px; color: #666;'>Recibimos una solicitud para restablecer tu contraseña.</p>" +
                "<p><a href='" + recoveryLink + "' " +
                "style='display: inline-block; padding: 12px 20px; background-color: #007bff; color: #fff; text-decoration: none; font-size: 14px; border-radius: 5px;'>Restablecer Contraseña</a></p>" +
                "<p style='font-size: 12px; color: #888;'>Si no solicitaste este cambio, ignora este correo.</p>" +
                "<p style='font-size: 14px;'><b>Equipo de GuacurariTech</b></p>" +
                "</body></html>";
    }

}
