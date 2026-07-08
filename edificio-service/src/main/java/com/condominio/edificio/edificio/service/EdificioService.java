package com.condominio.edificio.edificio.service;

import com.condominio.edificio.common.exception.BusinessException;
import com.condominio.edificio.common.exception.ResourceNotFoundException;
import com.condominio.edificio.common.pagination.PaginatedResponse;
import com.condominio.edificio.common.pagination.Pagination;
import com.condominio.edificio.edificio.dto.filter.EdificioFilter;
import com.condominio.edificio.edificio.dto.request.EdificioRequest;
import com.condominio.edificio.edificio.dto.response.EdificioDetailResponse;
import com.condominio.edificio.edificio.dto.response.EdificioResponse;
import com.condominio.edificio.edificio.entity.EdificioEntity;
import com.condominio.edificio.edificio.especification.EdificioSpecification;
import com.condominio.edificio.edificio.repository.EdificioRepository;
import com.condominio.edificio.edificio.repository.EmpresaRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@Slf4j
public class EdificioService {
    private final EmpresaRepository empresaRepository;
    private final EdificioRepository edificioRepository;
    private final ModelMapper modelMapper;

    public EdificioService(EmpresaRepository empresaRepository, EdificioRepository edificioRepository, ModelMapper modelMapper) {
        this.empresaRepository = empresaRepository;
        this.edificioRepository = edificioRepository;
        this.modelMapper = modelMapper;
    }

    @Transactional
    public EdificioResponse create(EdificioRequest edificioRequest) {
        //  Si envían idEmpresa validar que exista, ya que puede existir edificio sin empresa
        if (edificioRequest.getIdEmpresa() != null) {
            if (!empresaRepository.existsById(edificioRequest.getIdEmpresa())) {
                throw new ResourceNotFoundException("La empresa ingresada no existe");
            }
        }

        EdificioEntity edificioEntity = modelMapper.map(edificioRequest, EdificioEntity.class);
        edificioEntity.setNombre(edificioRequest.getNombre().trim().toUpperCase());
        edificioEntity.setEstado(true);

        if (Boolean.FALSE.equals(edificioEntity.getAplicaMora())) {
            edificioEntity.setMontoMora(BigDecimal.ZERO);
            edificioEntity.setPeriodoMora(null);
            edificioEntity.setDiaGracia(0);
        } else {
            if (edificioEntity.getPeriodoMora() == null) {
                throw new BusinessException("Debe ingresar un periodo de mora si indicó que se aplica mora");
            }
            if (edificioEntity.getMontoMora() == null || edificioEntity.getMontoMora().compareTo(BigDecimal.ZERO) <= 0) {
                throw new BusinessException("Debe ingresar un monto de mora mayor a 0 si indicó que se aplica mora");
            }
        }

        EdificioEntity edificioSaved = edificioRepository.save(edificioEntity);

        return modelMapper.map(edificioSaved, EdificioResponse.class);
    }

    @Transactional(readOnly = true)
    public EdificioDetailResponse findById(UUID id) {
        EdificioEntity edificioEntity = edificioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Edificio no encontrado"));
        return modelMapper.map(edificioEntity, EdificioDetailResponse.class);
    }

    @Transactional
    public void deleteEdificio(UUID id) {
        EdificioEntity edificioEntity = edificioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Edificio a eliminar no encontrado"));
        edificioRepository.delete(edificioEntity);
    }

    @Transactional
    public EdificioDetailResponse updateEdificio(UUID id, EdificioRequest edificioRequest) {
        EdificioEntity edificioEntity = edificioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Edificio no encontrado"));

        modelMapper.map(edificioRequest, edificioEntity);
        EdificioEntity saved = edificioRepository.save(edificioEntity);
        return modelMapper.map(saved, EdificioDetailResponse.class);
    }

    @Transactional(readOnly = true)
    public PaginatedResponse<EdificioDetailResponse> findByFilters(
            EdificioFilter filter,
            int page,
            int size,
            String sortBy,
            String direction
    ) {
        Sort sort = direction.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Specification<EdificioEntity> specification = EdificioSpecification.byFilters(filter);
        Page<EdificioDetailResponse> result = edificioRepository.findAll(specification, pageable).map(this::toResponse);

        Pagination pagination = new Pagination(
                result.getNumber(),
                result.getSize(),
                result.getTotalElements(),
                result.getTotalPages(),
                result.isFirst(),
                result.isLast(),
                result.hasNext(),
                result.hasPrevious()
        );
        return new PaginatedResponse<>(result.getContent(), pagination);
    }

    private EdificioDetailResponse toResponse(EdificioEntity entity) {
        return modelMapper.map(entity, EdificioDetailResponse.class);
    }
}
