package com.condominio.edificio.edificio.service;

import com.condominio.edificio.edificio.dto.response.CatalogoResponse;
import com.condominio.edificio.edificio.enums.*;
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

    public List<CatalogoResponse> listarTiposPropiedad() {

        return Arrays.stream(TipoPropiedad.values())
                .map(e -> new CatalogoResponse(
                        e.name(),
                        e.getDescripcion()))
                .toList();
    }

    public List<CatalogoResponse> listarTiposCobro() {

        return Arrays.stream(TipoCobro.values())
                .map(e -> new CatalogoResponse(
                        e.name(),
                        e.getDescripcion()))
                .toList();
    }

    public List<CatalogoResponse> listarPeriodoMora() {

        return Arrays.stream(PeriodoMora.values())
                .map(e -> new CatalogoResponse(
                        e.name(),
                        e.getDescripcion()))
                .toList();
    }
}
