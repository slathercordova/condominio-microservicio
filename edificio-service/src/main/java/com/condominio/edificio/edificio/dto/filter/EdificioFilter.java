package com.condominio.edificio.edificio.dto.filter;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@Getter
@Setter
@ToString
public class EdificioFilter {
    private UUID idEmpresa;
    private UUID id;
    private String nombre;
    private String ruc;
    private Boolean estado;
}
