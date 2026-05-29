package com.condominio.edificio.common.response;

import com.condominio.edificio.common.enums.ErrorCode;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)  //  Este comando es para devolver en el json solo los que tienen valor
@ToString
public class ApiResponse<T> {
    private boolean success;    //  true o false
    private String message;     //  resumen de errores si es que existen
    private ErrorCode errorCode;   //  codigo de error para log y front
    private T data;             //  devuelve la data si fue success
}
