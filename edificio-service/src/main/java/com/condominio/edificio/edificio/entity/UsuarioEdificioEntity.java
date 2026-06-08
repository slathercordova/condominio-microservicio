package com.condominio.edificio.edificio.entity;

import com.condominio.edificio.common.audit.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@DynamicUpdate
@Table(name = "usuario_edificio")
public class UsuarioEdificioEntity extends BaseEntity {
    @Column(name = "id_usuario", nullable = false)
    private UUID idUsuario;

    @Column(name = "id_edificio", nullable = false)
    private UUID idEdificio;

    @Column(name = "estado", nullable = false)
    private Boolean estado;
}
