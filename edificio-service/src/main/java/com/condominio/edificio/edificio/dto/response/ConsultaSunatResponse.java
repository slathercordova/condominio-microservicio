package com.condominio.edificio.edificio.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ConsultaSunatResponse(
        @JsonProperty("razon_social")
        String razonSocial,

        @JsonProperty("numero_documento")
        String numeroDocumento,

        String estado,

        String condicion,

        String direccion,

        String ubigeo,

        @JsonProperty("via_tipo")
        String viaTipo,

        @JsonProperty("via_nombre")
        String viaNombre,

        @JsonProperty("zona_codigo")
        String zonaCodigo,

        @JsonProperty("zona_tipo")
        String zonaTipo,

        String numero,

        String interior,

        String lote,

        String dpto,

        String manzana,

        String kilometro,

        String distrito,

        String provincia,

        String departamento,

        @JsonProperty("es_agente_retencion")
        Boolean esAgenteRetencion,

        @JsonProperty("es_buen_contribuyente")
        Boolean esBuenContribuyente,

        @JsonProperty("locales_anexos")
        Object localesAnexos,

        String tipo,

        @JsonProperty("actividad_economica")
        String actividadEconomica,

        @JsonProperty("numero_trabajadores")
        String numeroTrabajadores,

        @JsonProperty("tipo_facturacion")
        String tipoFacturacion,

        @JsonProperty("tipo_contabilidad")
        String tipoContabilidad,

        @JsonProperty("comercio_exterior")
        String comercioExterior
) {
}
