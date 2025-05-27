package com.pioneerPixel.BankService.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@NoArgsConstructor
@AllArgsConstructor
@ConfigurationProperties(prefix = "spring.security.jwt")
public class SecurityConstants {

    private String authHeader;
    private String bearerPrefix;
    private String accessSecret;
    private Integer accessLifetime;
    private String refreshSecret;
    private Integer refreshLifetime;
}
