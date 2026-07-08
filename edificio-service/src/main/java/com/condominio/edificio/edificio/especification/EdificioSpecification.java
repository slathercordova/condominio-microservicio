package com.condominio.edificio.edificio.especification;

import com.condominio.edificio.edificio.dto.filter.EdificioFilter;
import com.condominio.edificio.edificio.entity.EdificioEntity;
import jakarta.persistence.criteria.Predicate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class EdificioSpecification {
    public EdificioSpecification() {
    }

    public static Specification<EdificioEntity> byFilters(EdificioFilter filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filter.getId() != null) {
                predicates.add(cb.equal(root.get("id"), filter.getId()));
            }

            if (filter.getIdEmpresa() != null) {
                predicates.add(cb.equal(root.get("idEmpresa"), filter.getIdEmpresa()));
            }

            if (filter.getRuc() != null) {
                predicates.add(cb.equal(root.get("ruc"), filter.getRuc()));
            }

            if (filter.getNombre() != null) {
                predicates.add(cb.like(cb.upper(root.get("nombre")), "%" + filter.getRuc().toUpperCase().trim() + "%"));
            }

            if (filter.getEstado() != null) {
                predicates.add(cb.equal(root.get("estado"), filter.getEstado()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
