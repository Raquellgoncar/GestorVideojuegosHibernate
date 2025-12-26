/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo.util;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.util.Properties;

/**
 *
 * @author Raquel
 */
public class EmailService {

    private static final String EMAIL_FROM = "correopruebaraquel@gmail.com";
    private static final String PASSWORD = "zauvsbhpeqkjfyzs";

    public static void enviarNuevaPassword(String emailDestino, String nuevaPassword) {

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
                new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(EMAIL_FROM, PASSWORD);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(EMAIL_FROM));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(emailDestino)
            );
            message.setSubject("Recuperación de contraseña - CheckPoint");
            message.setText(
                    "Tu nueva contraseña es:\n\n"
                    + nuevaPassword
                    + "\n\nTe recomendamos cambiarla al iniciar sesión."
            );

            Transport.send(message);

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
