package com.condominio.edificio.edificio.repository;

import com.condominio.edificio.edificio.entity.PersonaUnidadEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface PersonaUnidadRepository extends JpaRepository<PersonaUnidadEntity, UUID>, JpaSpecificationExecutor<PersonaUnidadEntity> {
    boolean existsByIdUnidadAndEsResponsableTrueAndEstadoTrue(UUID idUnidad);

    boolean existsByIdUnidadAndEsResponsableTrueAndEstadoTrueAndIdNot(UUID idUnidad, UUID id);

    @Query(value = """
            select pu."id" as idPersonaUnidad, pu.id_persona as idPersona,
            u."id" as idUnidad, u.codigo, u.metraje, u.porcentaje, u.tipo_unidad as tipoUnidad,
            e."id" as idEdificio, e.nombre as edificioNombre, e.direccion as edificioDireccion
            from persona_unidad pu
            join unidad u on pu.id_unidad = u."id"
            join edificio e on u.id_edificio = e."id"
            where pu.id_persona = :idPersona
            """, nativeQuery = true)
    List<MisUnidadesProjection> listarMisUnidades(@Param("idPersona") UUID idPersona);

    @Query(value = """
            select pu."id" as idPersonaUnidad, pu.id_persona as idPersona,
            u."id" as idUnidad, u.codigo, u.metraje, u.porcentaje, u.tipo_unidad as tipoUnidad,
            e."id" as idEdificio, e.nombre as edificioNombre, e.direccion as edificioDireccion
            from persona_unidad pu
            join unidad u on pu.id_unidad = u."id"
            join edificio e on u.id_edificio = e."id"
            where pu.id_persona = :idPersona
            and pu.es_favorito = :esFavorito
            """, nativeQuery = true)
    List<MisUnidadesProjection> listarMisUnidadesFavorito(@Param("idPersona") UUID idPersona, @Param("esFavorito") boolean esFavorito);
}
