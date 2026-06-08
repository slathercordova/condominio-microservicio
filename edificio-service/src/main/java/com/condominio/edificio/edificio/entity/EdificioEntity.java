package com.condominio.edificio.edificio.entity;

import com.condominio.edificio.common.audit.BaseEntity;
import com.condominio.edificio.edificio.enums.PeriodoMora;
import com.condominio.edificio.edificio.enums.TipoCobro;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnTransformer;
import org.hibernate.annotations.DynamicUpdate;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@DynamicUpdate
@Table(name = "edificio")
public class EdificioEntity extends BaseEntity {
    @Column(name = "id_empresa")
    private UUID idEmpresa;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "logo_url", length = 300)
    private String logoUrl;

    @Column(name = "direccion", nullable = false, length = 250)
    private String direccion;

    @Column(name = "ruc", length = 11)
    private String ruc;

    @Column(name = "contingencia", precision = 6, scale = 2)  //6,2
    private BigDecimal contingencia;

    @Enumerated(EnumType.STRING)
    @ColumnTransformer(write = "?::tipo_cobro")
    @Column(name = "tipo_cobro", nullable = false)
    private TipoCobro tipoCobro;

    @Column(name = "aplica_mora",nullable = false)
    private Boolean aplicaMora;

    @Column(name = "monto_mora", precision = 10, scale = 2) //10,2
    private BigDecimal montoMora;

    @Enumerated(EnumType.STRING)
    @ColumnTransformer(write = "?::periodo_mora")
    @Column(name = "periodo_mora", nullable = false)
    private PeriodoMora periodoMora;

    @Column(name = "dia_generacion",nullable = false)
    private int diaGeneracion;

    @Column(name = "dia_vencimiento",nullable = false)
    private int diaVencimiento;

    @Column(name = "dia_gracia",nullable = false)
    private int diaGracia;

    @Column(name = "estado",nullable = false)
    private Boolean estado;
}
