package com.condominio.persona.service;

import com.condominio.persona.dto.request.TipoDocumentoRequest;
import com.condominio.persona.dto.response.TipoDocumentoResponse;
import com.condominio.persona.entity.TipoDocumentoEntity;
import com.condominio.persona.repository.TipoDocumentoRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class TipoDocumentoService {
    private final TipoDocumentoRepository tipoDocumentoRepository;
    private final ModelMapper modelMapper;

    public TipoDocumentoService(TipoDocumentoRepository tipoDocumentoRepository, ModelMapper modelMapper) {
        this.tipoDocumentoRepository = tipoDocumentoRepository;
        this.modelMapper = modelMapper;
    }

    public TipoDocumentoResponse createTipoDocumento(TipoDocumentoRequest tipoDocumentoRequest) {
        TipoDocumentoEntity tipDocEnt = new TipoDocumentoEntity();
        modelMapper.map(tipoDocumentoRequest, tipDocEnt);
        tipDocEnt.setCreatedBy(UUID.fromString("11111111-1111-1111-1111-111111111111"));

        TipoDocumentoEntity saved = tipoDocumentoRepository.save(tipDocEnt);

        TipoDocumentoResponse tipoDocumentoResponse = new TipoDocumentoResponse();
        modelMapper.map(saved, tipoDocumentoResponse);

        return tipoDocumentoResponse;
    }
}
