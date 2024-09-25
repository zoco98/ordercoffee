package com.example.coffee.order.machine.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "Coffee")
public class Coffee implements Serializable{
    private static final long serialVersionUID = 2L;
    @Id
    @Column(name = "COFFEENAME")
    private String CoffeeName;

    @Column(name = "COFFEESTOCK")
    @NotNull
    private Integer CoffeeStock;

    @Column(name = "ISCUSTOMIZED")
    private Boolean IsCustomized = Boolean.FALSE;

    @Column(name = "CUSTOMERSUGESSTION")
    private String CustomerSuggestion;
}
