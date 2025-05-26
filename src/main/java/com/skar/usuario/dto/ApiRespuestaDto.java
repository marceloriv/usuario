package com.skar.usuario.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApiRespuestaDto {

    private ApiRespuestaEstados estado;
    private String mensaje;

}
