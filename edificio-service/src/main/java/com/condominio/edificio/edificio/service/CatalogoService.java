package com.condominio.edificio.edificio.service;

import com.condominio.edificio.edificio.dto.response.CatalogoResponse;
import com.condominio.edificio.edificio.enums.TipoAlquiler;
import com.condominio.edificio.edificio.enums.TipoUnidad;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
public class CatalogoService {
    public List<CatalogoResponse> listarTiposUnidad() {

        return Arrays.stream(TipoUnidad.values())
                .map(e -> new CatalogoResponse(
                        e.name(),
                        e.getDescripcion()))
                .toList();
    }

    public List<CatalogoResponse> listarTiposAlquiler() {

        return Arrays.stream(TipoAlquiler.values())
                .map(e -> new CatalogoResponse(
                        e.name(),
                        e.getDescripcion()))
                .toList();
    }
}
