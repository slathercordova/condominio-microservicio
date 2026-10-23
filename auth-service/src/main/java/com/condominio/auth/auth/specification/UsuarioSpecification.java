package com.condominio.auth.auth.specification;

import com.condominio.auth.auth.dto.filter.UsuarioFilter;
import com.condominio.auth.auth.entity.UsuarioEntity;
import jakarta.persistence.criteria.Predicate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class UsuarioSpecification {
    public UsuarioSpecification() {
    }

    public static Specification<UsuarioEntity> byFilters(UsuarioFilter filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filter.getId() != null) {
                predicates.add(cb.equal(root.get("id"), filter.getId()));
            }

            if (filter.getIdPersona() != null) {
                predicates.add(cb.equal(root.get("idPersona"), filter.getIdPersona()));
            }

            if (filter.getUsername() != null) {
                predicates.add(cb.like(cb.upper(root.get("username")), "%" + filter.getUsername().trim().toUpperCase() + "%"));
            }

            if (filter.getCorreo() != null) {
                predicates.add(cb.equal(root.get("correo"), filter.getCorreo()));
            }

            if (filter.getTipoBloqueo() != null) {
                predicates.add(cb.equal(root.get("tipoBloqueo"), filter.getTipoBloqueo()));
            }

            if (filter.getEstado() != null) {
                predicates.add(cb.equal(root.get("estado"), filter.getEstado()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
