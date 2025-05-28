package com.pioneerPixel.BankService.config;

import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonModuleConfig {

    @Bean
    public SimpleModule javaTimeModule() {
        return new JavaTimeModule();
    }
}
