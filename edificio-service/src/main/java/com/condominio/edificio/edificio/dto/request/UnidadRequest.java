package com.condominio.edificio.edificio.dto.request;

import com.condominio.edificio.edificio.enums.TipoAlquiler;
import com.condominio.edificio.edificio.enums.TipoUnidad;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UnidadRequest {
    @NotNull(message = "Debe ingresar el edificio al que pertenece")
    private UUID idEdificio;

    @NotBlank(message = "Debe ingresar el código de esta unidad")
    @Size(max = 10, message = "Código máximo 10 caracteres")
    private String codigo;

    @Size(max = 300, message = "Logo Url máximo 300 caracteres")
    private String logoUrl;

    @NotNull(message = "Debe ingresa el piso")
    private Integer piso;

    @NotBlank(message = "Debe ingresar la torre")
    private String torre;

    @PositiveOrZero
    @NotNull(message = "Debe ingresar el metraje")
    private BigDecimal metraje;

    @PositiveOrZero
    @NotNull(message = "Debe ingresar 0 o un número positivo")
    private BigDecimal porcentaje;

    @NotNull(message = "Debe ingresar el tipo de unidad")
    private TipoUnidad tipoUnidad;

    private TipoAlquiler tipoAlquiler;

    @NotNull(message = "Debe ingresar un estado")
    private Boolean estado;
}
