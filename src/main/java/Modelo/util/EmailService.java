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
 * Servicio encargado del envío de correos electrónicos.
 * <p>
 * Esta clase se utiliza para enviar emails relacionados con la
 * recuperación de contraseña, enviando al usuario una nueva
 * contraseña generada previamente.
 * </p>
 *
 * @author Raquel
 * @version 1.0
 */
public class EmailService {

    /**
     * Dirección de correo desde la que se envían los mensajes.
     */
    private static final String EMAIL_FROM = "correopruebaraquel@gmail.com";

    /**
     * Contraseña de la cuenta de correo emisora.
     */
    private static final String PASSWORD = "zauvsbhpeqkjfyzs";

    /**
     * Envía un correo electrónico con una nueva contraseña al usuario.
     * <p>
     * Configura una sesión SMTP utilizando Gmail y envía un mensaje
     * con la nueva contraseña generada. Se recomienda al usuario
     * cambiarla tras iniciar sesión.
     * </p>
     *
     * @param emailDestino dirección de correo del destinatario
     * @param nuevaPassword nueva contraseña generada
     */
    public static void enviarNuevaPassword(String emailDestino, String nuevaPassword) {

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
                new Authenticator() {
            @Override
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
