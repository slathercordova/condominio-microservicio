package com.condominio.edificio.edificio.dto.response;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ListaEdificiosXUsuarioResponse {
    private UUID idEdificio;
    private String nombre;
}
