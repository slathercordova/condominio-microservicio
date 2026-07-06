package com.condominio.persona.persona.entity;

import com.condominio.persona.common.audit.BaseEntity;
import com.condominio.persona.common.enums.TipoSexo;
import com.condominio.persona.tipodocumento.entity.TipoDocumentoEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDate;

@Entity
@Table(name = "persona")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@DynamicUpdate
public class PersonaEntity extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tipo_documento", nullable = false)
    private TipoDocumentoEntity tipoDocumento;

    @Column(name = "numero_documento", nullable = false)
    private String numeroDocumento;

    @Column(name = "nacimiento", nullable = false)
    private LocalDate nacimiento;

    @Column(name = "celular")
    private String celular;

    @Column(name = "celular_2")
    private String celular2;

    @Column(name = "correo")
    private String correo;

    @Column(name = "correo_2")
    private String correo2;

    @Column(name = "nombres", nullable = false)
    private String nombres;

    @Column(name = "apellido_paterno", nullable = false)
    private String apellidoPaterno;

    @Column(name = "apellido_materno", nullable = false)
    private String apellidoMaterno;

    @Enumerated(EnumType.STRING)
    @Column(name = "sexo", nullable = false, columnDefinition = "sexo")
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
//    @ColumnTransformer(write = "?::tipo_sexo")
    private TipoSexo sexo;

    @Column(name = "estado", nullable = false)
    private Boolean estado;
}
