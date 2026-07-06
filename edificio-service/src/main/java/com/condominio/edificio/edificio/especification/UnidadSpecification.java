package com.condominio.edificio.edificio.especification;

import com.condominio.edificio.edificio.dto.filter.UnidadFilter;
import com.condominio.edificio.edificio.entity.UnidadEntity;
import jakarta.persistence.criteria.Predicate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class UnidadSpecification {
    public UnidadSpecification() {
    }

    public static Specification<UnidadEntity> byFilters(UnidadFilter filter) {
     return (root, query, cb) ->  {
        List<Predicate> predicates = new ArrayList<>();

        if (filter.getId() != null) {
            predicates.add(cb.equal(root.get("id"), filter.getId()));
        }

        if (filter.getIdEdificio() != null) {
            predicates.add(cb.equal(root.get("idEdificio"), filter.getIdEdificio()));
        }

        if (filter.getCodigo() != null) {
            predicates.add(cb.equal(root.get("codigo"), filter.getCodigo()));
        }

        if (filter.getPiso() != null) {
            predicates.add(cb.equal(root.get("piso"), filter.getPiso()));
        }

        if (filter.getMetraje() != null) {
            predicates.add(cb.equal(root.get("metraje"), filter.getMetraje()));
        }

        if (filter.getTipoUnidad() != null) {
            predicates.add(cb.equal(root.get("tipoUnidad"), filter.getTipoUnidad()));
        }

        if (filter.getTipoAlquiler() != null) {
            predicates.add(cb.equal(root.get("tipoAlquiler"), filter.getTipoAlquiler()));
        }

        if (filter.getEstado() != null) {
            predicates.add(cb.equal(root.get("estado"), filter.getEstado()));
        }

        return cb.and(predicates.toArray(new Predicate[0]));
    };
    }
}
