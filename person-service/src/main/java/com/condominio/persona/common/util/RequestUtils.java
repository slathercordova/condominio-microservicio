package com.condominio.persona.common.util;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

@Component
public class RequestUtils {
    public String getClientIp(HttpServletRequest request){

        String[] headers = {
                "X-Forwarded-For",
                "X-Real-IP",
                "Proxy-Client-IP",
                "WL-Proxy-Client-IP",
                "HTTP_X_FORWARDED_FOR",
                "HTTP_CLIENT_IP"
        };

        for(String header : headers){
            String value = request.getHeader(header);
            if(value != null
                    && !value.isBlank()
                    && !"unknown".equalsIgnoreCase(value)){

                return value.split(",")[0].trim();
            }
        }
        return request.getRemoteAddr();
    }

    public String getUserAgent(HttpServletRequest request){
        return request.getHeader("User-Agent");
    }
}
