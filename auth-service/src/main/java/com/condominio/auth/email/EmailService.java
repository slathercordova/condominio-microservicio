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
        String url = "http://localhost:5173/reset-password?token="+code;

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

            String html = """
                    <html>
                      <body style="margin:0;padding:0;background-color:#f3f4f6;font-family:Arial,sans-serif;">
                        <table width="100%" cellpadding="0" cellspacing="0" style="padding:40px 0;">
                          <tr>
                            <td align="center">
                              <!-- Contenedor principal -->
                              <table width="600" cellpadding="0" cellspacing="0"
                                     style="background:#ffffff;border-radius:12px;overflow:hidden;box-shadow:0 4px 20px rgba(0,0,0,0.1);">
                                <!-- Header -->
                                <tr>
                                  <td style="background:#1d4ed8;padding:20px;text-align:center;color:#ffffff;">
                                    <h2 style="margin:0;font-size:20px;">🏢 Condominio App</h2>
                                  </td>
                                </tr>
                                <!-- Body -->
                                <tr>
                                  <td style="padding:30px;text-align:center;">
                                    <h3 style="margin-bottom:10px;color:#111827;">
                                      Recuperación de contraseña
                                    </h3>
                                    <p style="color:#6b7280;font-size:14px;line-height:1.5;">
                                      Hemos recibido una solicitud para restablecer tu contraseña.
                                      Si fuiste tú, haz clic en el botón de abajo.
                                    </p>
                                    <!-- Botón -->
                                    <a href="{{URL}}"
                                       style="display:inline-block;margin-top:20px;padding:14px 24px;
                                              background:#2563eb;color:#ffffff;text-decoration:none;
                                              border-radius:8px;font-weight:bold;">
                                      Restablecer contraseña
                                    </a>
                                    <p style="margin-top:25px;font-size:12px;color:#9ca3af;">
                                      Este enlace expirará en 5 minutos por seguridad.
                                    </p>
                                  </td>
                                </tr>
                                <!-- Footer -->
                                <tr>
                                  <td style="padding:20px;background:#f9fafb;text-align:center;font-size:11px;color:#6b7280;">
                                    Si el botón no funciona, copia y pega este enlace:
                                    <br><br>
                                    <span style="word-break:break-all;color:#2563eb;">
                                      {{URL}}
                                    </span>
                                    <br><br>
                                    Si no solicitaste este cambio, puedes ignorar este correo.
                                  </td>
                                </tr>
                              </table>
                            </td>
                          </tr>
                        </table>
                      </body>
                    </html>
                """;

            String finalHtml = html.replace("{{URL}}",url);

            helper.setText(finalHtml, true);

            mailSender.send(mime);

        } catch (MessagingException e) {
            throw new RuntimeException(
                    "Error enviando correo"
            );
        }
    }
}