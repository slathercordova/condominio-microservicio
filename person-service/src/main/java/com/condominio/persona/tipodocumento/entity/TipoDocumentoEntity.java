package com.condominio.persona.tipodocumento.entity;

import com.condominio.persona.common.audit.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name = "tipo_documento")
@Getter
@Setter
@NoArgsConstructor
@DynamicUpdate
@ToString
public class TipoDocumentoEntity extends BaseEntity {
    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "nombre_corto", nullable = false)
    private String nombreCorto;

    @Column(name = "estado", nullable = false)
    private Boolean estado;

    public TipoDocumentoEntity(String nombre, String nombreCorto, boolean estado) {
        this.nombre = nombre;
        this.nombreCorto = nombreCorto;
        this.estado = estado;
    }
}
