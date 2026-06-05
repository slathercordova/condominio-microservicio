package com.condominio.edificio.edificio.dto.response;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class RolResponse {
    private UUID id;
    private String nombre;
    private Boolean estado;
}
