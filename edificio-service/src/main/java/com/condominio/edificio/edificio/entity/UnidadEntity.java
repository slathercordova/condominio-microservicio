package com.condominio.edificio.edificio.entity;

import com.condominio.edificio.common.audit.BaseEntity;
import com.condominio.edificio.edificio.enums.TipoAlquiler;
import com.condominio.edificio.edificio.enums.TipoUnidad;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.util.UUID;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@DynamicUpdate
@Table(name = "unidad")
public class UnidadEntity extends BaseEntity {
    @Column(name = "id_edificio", nullable = false)
    private UUID idEdificio;

    @Column(name = "codigo", nullable = false, length = 10)
    private String codigo;

    @Column(name = "logo_url", length = 300)
    private String logoUrl;

    @Column(name = "piso", nullable = false)
    private Integer piso;

    @Column(name = "torre", nullable = false, length = 10)
    private String torre;

    @Column(name = "metraje", nullable = false, precision = 10, scale = 3)
    private BigDecimal metraje;

    @Column(name = "porcentaje", nullable = false, precision = 6, scale = 3)
    private BigDecimal porcentaje;

    @Enumerated(EnumType.STRING)
//    @ColumnTransformer(write = "?::tipo_unidad")
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "tipo_unidad", nullable = false, columnDefinition = "tipo_unidad")
    private TipoUnidad tipoUnidad;

    @Enumerated(EnumType.STRING)
//    @ColumnTransformer(write = "?::tipo_alquiler")
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "tipo_alquiler", columnDefinition = "tipo_alquiler")
    private TipoAlquiler tipoAlquiler;

    @Column(name = "estado", nullable = false)
    private Boolean estado;
}