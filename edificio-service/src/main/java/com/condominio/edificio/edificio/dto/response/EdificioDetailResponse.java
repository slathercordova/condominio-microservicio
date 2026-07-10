package com.condominio.edificio.edificio.dto.response;

import com.condominio.edificio.edificio.enums.PeriodoMora;
import com.condominio.edificio.edificio.enums.TipoCobro;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class EdificioDetailResponse {
    private  UUID id;
    private UUID idEmpresa;
    private String nombre;
    private String logoUrl;
    private String direccion;
    private String ruc;
    private BigDecimal contingencia;
    private TipoCobro tipoCobro;
    private Boolean aplicaMora;
    private BigDecimal montoMora;
    private PeriodoMora periodoMora;
    private Integer diaGeneracion;
    private Integer diaVencimiento;
    private Integer diaGracia;
    private BigDecimal gastoTotal ;
    private Boolean estado;
}
