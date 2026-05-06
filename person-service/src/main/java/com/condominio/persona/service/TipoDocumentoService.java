package com.condominio.persona.service;

import com.condominio.persona.dto.request.TipoDocumentoRequest;
import com.condominio.persona.dto.response.TipoDocumentoDetailResponse;
import com.condominio.persona.dto.response.TipoDocumentoResponse;
import com.condominio.persona.entity.TipoDocumentoEntity;
import com.condominio.persona.exception.ResourceNotFoundException;
import com.condominio.persona.repository.TipoDocumentoRepository;
import com.condominio.persona.util.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
public class TipoDocumentoService {
    private final TipoDocumentoRepository tipoDocumentoRepository;
    private final ModelMapper modelMapper;
    private final SecurityUtils securityUtils;

    public TipoDocumentoService(TipoDocumentoRepository tipoDocumentoRepository, ModelMapper modelMapper, SecurityUtils securityUtils) {
        this.tipoDocumentoRepository = tipoDocumentoRepository;
        this.modelMapper = modelMapper;
        this.securityUtils = securityUtils;
    }

    public TipoDocumentoResponse createTipoDocumento(TipoDocumentoRequest tipoDocumentoRequest) {
        log.info(">>>>>>>>Creando Tipo Documento");
        log.debug("Request : {}", tipoDocumentoRequest);

        formatearDatos(tipoDocumentoRequest);
        TipoDocumentoEntity tipDocEnt = new TipoDocumentoEntity();
        modelMapper.map(tipoDocumentoRequest, tipDocEnt);
        tipDocEnt.setCreatedBy(securityUtils.getCurrentUserId());//todo cambiar con auth

        TipoDocumentoEntity saved = tipoDocumentoRepository.save(tipDocEnt);

        TipoDocumentoResponse tipoDocumentoResponse = new TipoDocumentoResponse();
        modelMapper.map(saved, tipoDocumentoResponse);

        return tipoDocumentoResponse;
    }

    public void deleteTipoDocumento(UUID id) {
        log.info(">>>>>>>>Eliminando Tipo Documento");

        TipoDocumentoEntity tipDocEnt = tipoDocumentoRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Id no encontrado"));
        tipoDocumentoRepository.delete(tipDocEnt);
    }

    public TipoDocumentoDetailResponse updateTipoDocumento(UUID id, TipoDocumentoRequest tipoDocumentoRequest) {
        log.info(">>>>>>>>Update Tipo Documento");
        log.debug("Request: {}", tipoDocumentoRequest);

        formatearDatos(tipoDocumentoRequest);

        TipoDocumentoEntity entity = tipoDocumentoRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Id no encontrado"));

        modelMapper.map(tipoDocumentoRequest, entity);  //  actualiza al existente
        entity.setUpdatedBy(securityUtils.getCurrentUserId());//todo cambiar con auth
        TipoDocumentoEntity saved = tipoDocumentoRepository.save(entity);

        return modelMapper.map(saved, TipoDocumentoDetailResponse.class);
    }

    public void formatearDatos(TipoDocumentoRequest tipoDocumentoRequest) {
        //TipoDocumentoRequest formatRequest = new TipoDocumentoRequest();
        //modelMapper.map(TipoDocumentoRequest, formatRequest);
        tipoDocumentoRequest.setNombre(tipoDocumentoRequest.getNombre().toUpperCase());
        tipoDocumentoRequest.setNombreCorto(tipoDocumentoRequest.getNombreCorto().toUpperCase());
    }
}
