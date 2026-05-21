package com.condominio.persona.tipodocumento.service;

import com.condominio.persona.common.exception.ResourceAlreadyExistsException;
import com.condominio.persona.tipodocumento.dto.filter.TipoDocumentoFilter;
import com.condominio.persona.tipodocumento.dto.request.TipoDocumentoRequest;
import com.condominio.persona.tipodocumento.dto.response.TipoDocumentoDetailResponse;
import com.condominio.persona.tipodocumento.dto.response.TipoDocumentoResponse;
import com.condominio.persona.tipodocumento.entity.TipoDocumentoEntity;
import com.condominio.persona.common.exception.ResourceNotFoundException;
import com.condominio.persona.tipodocumento.repository.TipoDocumentoRepository;
import com.condominio.persona.tipodocumento.specification.TipoDocumentoSpecification;
import com.condominio.persona.common.util.PaginatedResponse;
import com.condominio.persona.common.util.Pagination;
import com.condominio.persona.common.util.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    @Transactional
    public TipoDocumentoResponse createTipoDocumento(TipoDocumentoRequest tipoDocumentoRequest) {
        log.info("Creando Tipo Documento");
        log.debug("Request : {}", tipoDocumentoRequest);

        formatearDatos(tipoDocumentoRequest);

        if(tipoDocumentoRepository.existsByNombre(tipoDocumentoRequest.getNombre())){
            throw new ResourceAlreadyExistsException("Ya existe un tipo de documento con ese nombre");
        }

        TipoDocumentoEntity tipDocEnt = new TipoDocumentoEntity();
        modelMapper.map(tipoDocumentoRequest, tipDocEnt);

        TipoDocumentoEntity saved = tipoDocumentoRepository.save(tipDocEnt);

        return modelMapper.map(saved, TipoDocumentoResponse.class);
    }

    @Transactional
    public void deleteTipoDocumento(UUID id) {
        log.info("Eliminando Tipo Documento");

        TipoDocumentoEntity tipDocEnt = tipoDocumentoRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Id no encontrado"));
        tipoDocumentoRepository.delete(tipDocEnt);
    }

    @Transactional
    public TipoDocumentoDetailResponse updateTipoDocumento(UUID id, TipoDocumentoRequest tipoDocumentoRequest) {
        log.info("Update Tipo Documento");
        log.debug("Request: {}", tipoDocumentoRequest);

        formatearDatos(tipoDocumentoRequest);

        if(tipoDocumentoRepository.existsByNombreAndIdNot(tipoDocumentoRequest.getNombre(),id)){
            throw new ResourceAlreadyExistsException("Ya existe un tipo de documento con ese nombre");
        }

        TipoDocumentoEntity entity = tipoDocumentoRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Id no encontrado"));

        modelMapper.map(tipoDocumentoRequest, entity);  //  actualiza al existente
        TipoDocumentoEntity saved = tipoDocumentoRepository.save(entity);

        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public TipoDocumentoDetailResponse getTipoDocumento(UUID id) {
        log.info("Retornando Tipo Documento");

        TipoDocumentoEntity entity = tipoDocumentoRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Id no encontrado"));
        return toResponse(entity);
    }

    @Transactional(readOnly = true)
    public PaginatedResponse<TipoDocumentoDetailResponse> findByFilters(
            TipoDocumentoFilter filter,
            int page,
            int size,
            String sortBy,
            String direction
            ) {

        log.info("FILTER: {}", filter);

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Specification<TipoDocumentoEntity> specification = TipoDocumentoSpecification.byFilters(filter);

        Page<TipoDocumentoDetailResponse> result = tipoDocumentoRepository.findAll(specification,pageable).map(this::toResponse);

        Pagination pagination = new Pagination(
                result.getNumber(),
                result.getSize(),
                result.getTotalElements(),
                result.getTotalPages(),
                result.isFirst(),
                result.isLast(),
                result.hasNext(),
                result.hasPrevious());

        return new PaginatedResponse<>(result.getContent(),pagination);
    }




    private void formatearDatos(TipoDocumentoRequest tipoDocumentoRequest) {
        tipoDocumentoRequest.setNombre(tipoDocumentoRequest.getNombre().trim().toUpperCase());
        tipoDocumentoRequest.setNombreCorto(tipoDocumentoRequest.getNombreCorto().trim().toUpperCase());
    }

    private TipoDocumentoDetailResponse toResponse(TipoDocumentoEntity entity) {
        return modelMapper.map(entity, TipoDocumentoDetailResponse.class);
    }
}
