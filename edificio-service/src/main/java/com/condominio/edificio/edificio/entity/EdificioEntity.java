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
@Table(name = "empresa")
public class EdificioEntity extends BaseEntity {
    @Column(name = "razon_Social", nullable = false, length = 100)
    private String razonSocial;

    @Column(name = "direccion", nullable = false, length = 250)
    private String direccion;

    @Column(name = "ruc", nullable = false, length = 11)
    private String ruc;

    @Column(name = "telefono", length = 20)
    private String telefono;

    @Column(name = "celular", length = 20)
    private String celular;

    @Column(name = "correo", length = 100)
    private String correo;

    @Column(name = "id_representante", nullable = false)
    private UUID idRepresentante;

    @Column(name = "logo_url", length = 300)
    private String logoUrl;

    @Column(name = "estado", nullable = false)
    private Boolean estado;
}
