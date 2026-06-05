package com.condominio.edificio.edificio.entity;

import com.condominio.edificio.common.audit.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@DynamicUpdate
@Table(name = "rol")
public class RolEntity extends BaseEntity {
    @Column(name = "nombre", nullable = false, length = 30)
    private String nombre;

    @Column(name = "estado", nullable = false)
    private Boolean estado;
}