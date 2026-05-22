package com.condominio.auth.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendRecoveryCode(
            String to,
            String subject,
            String code
    ) {

        try {

            MimeMessage mime =
                    mailSender.createMimeMessage();

            MimeMessageHelper helper =
                    new MimeMessageHelper(
                            mime,
                            true,
                            "UTF-8"
                    );

            helper.setTo(to);
            helper.setSubject(subject);

            helper.setText("""
                    <html>
                    <body>
                        <h2>Recuperar contraseña</h2>
                        
                        <p>Tu código es:</p>
                        
                        <div style="
                        font-size:30px;
                        font-weight:bold;
                        padding:10px;
                        background:#f3f3f3;
                        width:120px;
                        text-align:center;
                        ">
                        %s
                        </div>

                        <p>
                        Este código expira en 10 minutos.
                        </p>

                    </body>
                    </html>
                    """.formatted(code), true);

            mailSender.send(mime);

        } catch (MessagingException e) {
            throw new RuntimeException(
                    "Error enviando correo"
            );
        }
    }
}