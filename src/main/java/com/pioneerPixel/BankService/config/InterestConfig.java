package com.pioneerPixel.BankService.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@ConfigurationProperties(prefix = "app.interest")
@Getter
@Setter
public class InterestConfig {
    private BigDecimal multiplier;
    private BigDecimal maxRatio;
}
