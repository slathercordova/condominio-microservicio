package com.condominio.edificio.edificio.service;

import com.condominio.edificio.common.exception.BusinessException;
import com.condominio.edificio.common.exception.ResourceNotFoundException;
import com.condominio.edificio.common.pagination.PaginatedResponse;
import com.condominio.edificio.common.pagination.Pagination;
import com.condominio.edificio.edificio.dto.filter.UnidadFilter;
import com.condominio.edificio.edificio.dto.request.UnidadRequest;
import com.condominio.edificio.edificio.dto.response.UnidadDetailResponse;
import com.condominio.edificio.edificio.dto.response.UnidadResponse;
import com.condominio.edificio.edificio.entity.EdificioEntity;
import com.condominio.edificio.edificio.entity.UnidadEntity;
import com.condominio.edificio.edificio.enums.TipoCobro;
import com.condominio.edificio.edificio.especification.UnidadSpecification;
import com.condominio.edificio.edificio.repository.EdificioRepository;
import com.condominio.edificio.edificio.repository.UnidadRepository;
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
import java.math.RoundingMode;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class UnidadService {
    private final UnidadRepository unidadRepository;
    private final EdificioRepository edificioRepository;
    private final ModelMapper modelMapper;

    public UnidadService(UnidadRepository unidadRepository, EdificioRepository edificioRepository, ModelMapper modelMapper) {
        this.unidadRepository = unidadRepository;
        this.edificioRepository = edificioRepository;
        this.modelMapper = modelMapper;
    }

    @Transactional
    public UnidadResponse create(UnidadRequest unidadRequest) {
        //  validamos que el edificio exista
        if (!edificioRepository.existsById(unidadRequest.getIdEdificio())) {
            throw new ResourceNotFoundException("Edificio no encontrado");
        }

        //  guardamos la unidad
        UnidadEntity unidadEntity = modelMapper.map(unidadRequest, UnidadEntity.class);
        unidadEntity.setEstado(true);

        UnidadEntity unidadSaved = unidadRepository.save(unidadEntity);

        return modelMapper.map(unidadSaved, UnidadResponse.class);
    }

    @Transactional
    public Integer calcularPorcentajes(UUID idEdificio) {
        EdificioEntity edificioEntity = edificioRepository.findById(idEdificio).orElseThrow(() -> new ResourceNotFoundException("Edificio no encontrado"));

        if (!edificioRepository.existsById(idEdificio)) {
            throw new ResourceNotFoundException("Edificio no encontrado");
        }

        List<UnidadEntity> listaUnidades = unidadRepository.findByIdEdificioAndEstadoTrue(idEdificio);
        if (listaUnidades.isEmpty()) {
            throw new BusinessException("No hay unidades en el edificio que quiere calcular");
        }

        //  Verificamos que cfg tiene el edificio
        TipoCobro tipoCobro = edificioEntity.getTipoCobro();
        switch (tipoCobro) {
            case PORCENTAJE -> {
                System.out.println("Opción PORCENTAJE");

                //  Calculamos metraje total
                BigDecimal metrajeTotal = BigDecimal.ZERO;

                for (UnidadEntity item : listaUnidades) {
                    metrajeTotal = metrajeTotal.add(item.getMetraje());
                }

                if (metrajeTotal.compareTo(BigDecimal.ZERO) <= 0) {
                    throw new BusinessException("El metraje total del edificio debe ser mayor a cero");
                }

                BigDecimal cien = BigDecimal.valueOf(100);

                //  Calculamos el % de participación de cada depa
                for (UnidadEntity item : listaUnidades) {
                    BigDecimal porcentajeTmp = (item.getMetraje().divide(metrajeTotal, 6, RoundingMode.HALF_UP)).multiply(cien).setScale(3, RoundingMode.HALF_UP);
                    item.setPorcentaje(porcentajeTmp);
                }
            }

            case FLAT -> {
                System.out.println("Opción FLAT");
                //  Cantidad total de depas
                int totalUnidades = listaUnidades.size();

                if (totalUnidades <= 0) {
                    throw new BusinessException("El total de unidades del edificio debe ser mayor a cero");
                }

                BigDecimal cien = BigDecimal.valueOf(100);

                //  Calculamos el % de participación de cada depa
                BigDecimal porcentajeTmp = (cien.divide(BigDecimal.valueOf(totalUnidades), 6, RoundingMode.HALF_UP)).setScale(3, RoundingMode.HALF_UP);
                for (UnidadEntity item : listaUnidades) {
                    item.setPorcentaje(porcentajeTmp);
                }
            }
            default -> {
                System.out.println("Opción no válida");
                throw new BusinessException("La distribución del edificio no es válida por el momento");
            }
        }
        //  actualizamos el metraje global de edificio con la suma total de todas las unidades...
        unidadRepository.saveAll(listaUnidades);
        return listaUnidades.size();
    }

    @Transactional(readOnly = true)
    public UnidadDetailResponse detailUnidad(UUID id) {
        if (id == null) {
            throw new BusinessException("No ingresó el ID de la unidad");
        }

        UnidadEntity unidad = unidadRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Unidad no encontrada"));

        return modelMapper.map(unidad, UnidadDetailResponse.class);
    }

    @Transactional
    public void deleteUnidad(UUID id) {
        UnidadEntity unidadEntity = unidadRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Unidad a eliminar no encontrada"));
        unidadRepository.delete(unidadEntity);
    }

    @Transactional
    public UnidadDetailResponse updateUnidad(UUID id, UnidadRequest unidadRequest) {

        UnidadEntity unidadEntity = unidadRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Unidad no encontrada"));

//        todo controlar reglas de negocio de unidad, repetir código

        modelMapper.map(unidadRequest, unidadEntity);
        UnidadEntity saved = unidadRepository.save(unidadEntity);
        return modelMapper.map(saved, UnidadDetailResponse.class);
    }

    @Transactional(readOnly = true)
    public PaginatedResponse<UnidadDetailResponse> findByFilters(UnidadFilter filter, int page, int size, String sortBy, String direction) {
        Sort sort = direction.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Specification<UnidadEntity> specification = UnidadSpecification.byFilters(filter);
        Page<UnidadDetailResponse> result = unidadRepository.findAll(specification, pageable).map(this::toResponse);

        Pagination pagination = new Pagination(result.getNumber(), result.getSize(), result.getTotalElements(), result.getTotalPages(), result.isFirst(), result.isLast(), result.hasNext(), result.hasPrevious());
        return new PaginatedResponse<>(result.getContent(), pagination);
    }

    private UnidadDetailResponse toResponse(UnidadEntity entity) {
        return modelMapper.map(entity, UnidadDetailResponse.class);
    }
}
