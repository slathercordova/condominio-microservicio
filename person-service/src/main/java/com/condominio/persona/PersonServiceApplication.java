package com.condominio.persona;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableFeignClients
@EnableJpaAuditing(
        auditorAwareRef = "auditorAware",
        modifyOnCreate = false
)
@EnableDiscoveryClient
public class PersonServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(PersonServiceApplication.class, args);
    }
}
