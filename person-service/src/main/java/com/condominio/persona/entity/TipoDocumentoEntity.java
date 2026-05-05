package com.condominio.persona.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tipo_documento")
@Getter
@Setter
@NoArgsConstructor
public class TipoDocumentoEntity extends BaseEntity{
    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "nombre_corto", nullable = false)
    private String nombreCorto;

    @Column(name = "estado", nullable = false)
    private boolean estado;

    public TipoDocumentoEntity(String nombre, String nombreCorto, boolean estado) {
        this.nombre = nombre;
        this.nombreCorto = nombreCorto;
        this.estado = estado;
    }
}
