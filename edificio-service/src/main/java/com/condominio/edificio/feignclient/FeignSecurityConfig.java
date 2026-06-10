package com.condominio.edificio.feignclient;

import feign.RequestInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

//@Configuration
public class FeignSecurityConfig {

    @Bean
    public RequestInterceptor requestInterceptor(){
        return template -> {
            RequestAttributes attributes = RequestContextHolder.getRequestAttributes();

            if(attributes == null){
                return;
            }

            HttpServletRequest request = ((ServletRequestAttributes) attributes).getRequest();

            String authHeader = request.getHeader("Authorization");

            if(authHeader != null){
                template.header("Authorization",authHeader);
            }
        };
    }
}
