package com.condominio.edificio.feignclient;

import com.condominio.edificio.edificio.dto.response.ConsultaSunatResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "sunat-client-ws", url = "${sunat.url}", configuration = FeignExternalConfig.class)
public interface SunatClientWs {
    @GetMapping
    ConsultaSunatResponse getSunat(@RequestParam String numero, @RequestHeader("Authorization") String token);
}
