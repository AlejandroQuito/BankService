package com.pioneerPixel.BankService.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "account")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "balance",
            nullable = false,
            precision = 19, scale = 2)
    private BigDecimal balance;

    @Column(name = "initial_balance",
            nullable = false,
            precision = 19, scale = 2)
    private BigDecimal initialBalance;

    @OneToOne
    @JoinColumn(name = "user_id",
            nullable = false,
            unique = true)
    private User user;
}
