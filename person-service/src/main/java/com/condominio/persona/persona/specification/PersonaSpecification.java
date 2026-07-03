package com.condominio.persona.persona.specification;

import com.condominio.persona.persona.dto.filter.PersonaFilter;
import com.condominio.persona.persona.entity.PersonaEntity;
import jakarta.persistence.criteria.Predicate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class PersonaSpecification {
    public PersonaSpecification() {
    }

    public static Specification<PersonaEntity> byFilters(PersonaFilter filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filter.getId() != null) {
                predicates.add(cb.equal(root.get("id"), filter.getId()));
            }

            if (filter.getNombres() != null && !filter.getNombres().isBlank()) {
                predicates.add(cb.like(cb.upper(root.get("nombres")), "%" + filter.getNombres().trim().toUpperCase() + "%"));
            }

            if (filter.getApellidoPaterno() != null && !filter.getApellidoPaterno().isBlank()) {
                predicates.add(cb.like(cb.upper(root.get("apellidoPaterno")), "%" + filter.getApellidoPaterno().trim().toUpperCase() + "%"));
            }

            if (filter.getEstado() != null) {
                predicates.add(cb.equal(root.get("estado"), filter.getEstado()));
            }

            if (filter.getTipoDocumento() != null) {
                predicates.add(cb.equal(root.get("tipoDocumento"), filter.getTipoDocumento()));
            }

            if (filter.getNumeroDocumento() != null) {
                predicates.add(cb.equal(root.get("numeroDocumento"), filter.getNumeroDocumento()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
