package com.condominio.auth.auth.entity;

import com.condominio.auth.common.BaseEntity;
import com.condominio.auth.common.enums.EstadoUsuario;
import com.condominio.auth.common.enums.TipoBloqueo;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnTransformer;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "usuario")
public class UsuarioEntity extends BaseEntity {
    @Column(name = "id_persona", nullable = false)
    private UUID idPersona;

    @Column(name = "username", nullable = false, length = 30)
    private String username;

    @Column(name = "password", nullable = false, length = 255)
    private String password;

    @Column(name = "correo", length = 100)
    private String correo;

    @Column(name = "correo_2", length = 100)
    private String correo2;

    @Column(name = "ultimo_login")
    private OffsetDateTime ultimoLogin;

    @Column(name = "intento_erroneo", nullable = false)
    private short intentoErroneo;

    @Column(name = "bloqueo_at")
    private OffsetDateTime bloqueoAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_bloqueo", nullable = false)
    @ColumnTransformer(write = "?::tipo_bloqueo")
    private TipoBloqueo tipoBloqueo;

    @Column(name = "primera_vez", nullable = false)
    private boolean primeraVez;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    @ColumnTransformer(write = "?::estado_usuario")
    private EstadoUsuario estado;
}
