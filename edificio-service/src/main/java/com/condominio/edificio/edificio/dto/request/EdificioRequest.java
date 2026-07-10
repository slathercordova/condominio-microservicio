package com.condominio.edificio.edificio.dto.request;

import com.condominio.edificio.edificio.enums.PeriodoMora;
import com.condominio.edificio.edificio.enums.TipoCobro;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class EdificioRequest {
    private UUID idEmpresa;

    @NotBlank(message = "Debe ingresar nombre del edificio")
    @Size(max = 100, message = "Nombre de edificio máximo 100 caracteres")
    private String nombre;

    @Size(max = 300, message = "Logo Url máximo 300 caracteres")
    private String logoUrl;

    @NotBlank(message = "Debe ingresar una dirección")
    @Size(max = 250, message = "Dirección máximo 250 caracteres")
    private String direccion;

    @Size(max = 11, message = "Ruc máximo 11 dígitos")
    @Pattern(
            regexp = "^$|^[0-9]{11}$",
            message = "El RUC debe tener exactamente 11 dígitos"
    )
    private String ruc;

    @DecimalMin(value = "0.00", message = "La contingencia debe estar en un rango de 0 a 100%")
    @DecimalMax(value = "100.00", message = "La contingencia debe estar en un rango de 0 a 100%")
    @Digits(integer = 3, fraction = 2)
    private BigDecimal contingencia;

    @NotNull(message = "Debe ingresar el tipo de cobro")
    private TipoCobro tipoCobro;

    @NotNull(message = "Debe indicar si se aplica mora")
    private Boolean aplicaMora;

    @PositiveOrZero(message = "Monto mora no puede ser negativo")
    @Digits(integer = 8, fraction = 2)
    private BigDecimal montoMora;

    private PeriodoMora periodoMora;

    @NotNull(message = "Debe de ingresar un día de generación de recibos")
    @Min(value = 1, message = "Dia de generación de recibo debe estar entre 1 y 31")
    @Max(value = 31, message = "Dia de generación de recibo debe estar entre 1 y 31")
    private Integer diaGeneracion;

    @NotNull(message = "Debe de ingresar un día de vencimiento de recibos")
    @Min(value = 1, message = "Dia de vencimiento de recibo debe estar entre 1 y 31")
    @Max(value = 31, message = "Dia de vencimiento de recibo debe estar entre 1 y 31")
    private Integer diaVencimiento;

    @NotNull(message = "Debe de ingresar cantidad de día de gracia")
    @Min(value = 0, message = "Dia de gracia no puede ser negativo")
    private Integer diaGracia;

    @PositiveOrZero(message = "El gasto total debe ser igual o mayor a 0")
    private BigDecimal gastoTotal;
}
