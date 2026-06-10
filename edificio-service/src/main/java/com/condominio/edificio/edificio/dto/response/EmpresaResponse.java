package com.condominio.edificio.edificio.dto.response;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class EmpresaResponse {
    private UUID id;
    private String ruc;
    private String razonSocial;
    private String direccion;
}
