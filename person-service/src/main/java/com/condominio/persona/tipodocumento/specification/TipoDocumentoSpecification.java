package com.condominio.persona.tipodocumento.specification;

import com.condominio.persona.tipodocumento.dto.filter.TipoDocumentoFilter;
import com.condominio.persona.tipodocumento.entity.TipoDocumentoEntity;
import jakarta.persistence.criteria.Predicate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class TipoDocumentoSpecification {

    private TipoDocumentoSpecification() {
    }

    public static Specification<TipoDocumentoEntity> byFilters(TipoDocumentoFilter filter) {

        return (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            if (filter.getId() != null) {
                predicates.add(cb.equal(root.get("id"), filter.getId()));
            }

            if (filter.getNombre() != null && !filter.getNombre().isBlank()) {
                predicates.add(cb.like(cb.upper(root.get("nombre")),"%" + filter.getNombre().trim().toUpperCase() + "%"));
            }

            if (filter.getNombreCorto() != null && !filter.getNombreCorto().isBlank()) {
                predicates.add(cb.like(cb.upper(root.get("nombreCorto")),"%" + filter.getNombreCorto().trim().toUpperCase() + "%"));
            }

            if (filter.getEstado() != null) {
                predicates.add(cb.equal(root.get("estado"), filter.getEstado()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}