package com.condominio.auth.auth.entity;

import com.condominio.auth.auth.enums.EstadoUsuario;
import com.condominio.auth.auth.enums.TipoBloqueo;
import com.condominio.auth.common.audit.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@DynamicUpdate
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
    private Instant ultimoLogin;

    @Column(name = "intento_erroneo", nullable = false)
    private Integer intentoErroneo;

    @Column(name = "bloqueo_at")
    private Instant bloqueoAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_bloqueo", nullable = false, columnDefinition = "tipo_bloqueo")
//    @ColumnTransformer(write = "?::tipo_bloqueo")
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    private TipoBloqueo tipoBloqueo;

    @Column(name = "primera_vez", nullable = false)
    private boolean primeraVez;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, columnDefinition = "estado")
//    @ColumnTransformer(write = "?::estado_usuario")
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    private EstadoUsuario estado;
}
