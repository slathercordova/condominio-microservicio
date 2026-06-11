package com.condominio.edificio.edificio.entity;

import com.condominio.edificio.common.audit.BaseEntity;
import com.condominio.edificio.edificio.enums.TipoPropiedad;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnTransformer;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@DynamicUpdate
@Table(name = "persona_unidad")
public class PersonaUnidadEntity extends BaseEntity {

    @Column(name = "id_unidad", nullable = false)
    private UUID idUnidad;

    @Column(name = "id_persona", nullable = false)
    private UUID idPersona;

    @Column(name = "es_responsable", nullable = false)
    private Boolean esResponsable;

    @Column(name = "fecha_inicio", nullable = false)
    private LocalDate fechaInicio;

    @Column(name = "fecha_fin")
    private LocalDate fechaFin;

    @Enumerated(EnumType.STRING)
    @ColumnTransformer(write = "?::tipo_propiedad")
    @Column(name = "tipo_propiedad", nullable = false)
    private TipoPropiedad tipoPropiedad;

    @Column(name = "estado", nullable = false)
    private Boolean estado;
}
