package com.condominio.auth.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    @Async
    public void sendRecoveryCode(
            String to,
            String code
    ) {
        String url = "https://www.miapp-condominio.com.pe/reset-password?token="+code;

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
            helper.setSubject("Recuperación de contraseña");

            helper.setText("""
                    <html>
                        <body>
                           <h2 style="
                                background-color: black;
                                font-size: 30px;
                                color: white;
                                text-align: center;
                                padding: 10px;">
                                Recuperar contraseña</h2>
    
                           <div style="text-align: center;">
                               <a href="%s" target="_blank" style="
                                       background:#eb3737;
                                       padding:15px;
                                       color:white;
                                       text-decoration:none;
                                       border-radius:8px;
                                       font-weight:bold;
                                       ">Restablecer contraseña</a>
                               <p style="font-size:12px;color:gray"><br>
                                   Si el botón no funciona copie este enlace:</p>
                           </div>
    
                           <div style="background-color: black">
    
                               <p style="
                                                  font-size:11px;
                                                  word-break:break-all;
                                                  color: white;
                                                  ">
                                   %s
                               </p>
                               <p style="font-size: 12px; color: white;">Enlace expira en 5 minutos.</p>
                           </div>
                        </body>
                    </html>
                    """.formatted(url, url), true);

            mailSender.send(mime);

        } catch (MessagingException e) {
            throw new RuntimeException(
                    "Error enviando correo"
            );
        }
    }
}