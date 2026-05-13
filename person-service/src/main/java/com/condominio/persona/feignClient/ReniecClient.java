package com.condominio.persona.feignClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "reniec-client-ws", url = "${reniec.url}")
public interface ReniecClient {
    @GetMapping
    ReniecResponseDto getReniec(@RequestParam String numero, @RequestHeader("Authorization") String token);
}
