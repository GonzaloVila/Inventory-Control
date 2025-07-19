package com.example.inventorycontrol.config;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.datatype.hibernate6.Hibernate6Module;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig {

    @Bean
    public Module hibernateModule() {
        // Desactiva la escritura de los objetos ID, para evitar problemas si las entidades related tienen IDs compuestas
        return new Hibernate6Module().disable(Hibernate6Module.Feature.FORCE_LAZY_LOADING);
    }
}
