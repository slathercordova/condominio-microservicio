package com.condominio.auth.auth.entity;

import com.condominio.auth.common.audit.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@DynamicUpdate
@Table(name = "refresh_token")
public class RefreshTokenEntity extends BaseEntity {
    @Column(name = "usuario_id", nullable = false)
    private UUID usuarioId;

    @Column(name = "token_hash", nullable = false, length = 255)
    private String tokenHash;

    @Column(name = "expiracion_at", nullable = false)
    private Instant expiracionAt;

    @Column(name = "usado", nullable = false)
    private Boolean usado;

    @Column(name = "revocado", nullable = false)
    private Boolean revocado;

    @Column(name = "dispositivo", length = 300)
    private String dispositivo;

    @Column(name = "ip", length = 15)
    private String ip;
}
