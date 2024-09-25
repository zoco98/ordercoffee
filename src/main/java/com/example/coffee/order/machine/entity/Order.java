package com.example.coffee.order.machine.entity;

import jakarta.persistence.*;
import jakarta.transaction.UserTransaction;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "CoffeeOrder")
public class Order implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "ORDERID")
    private String orderId;

    @Column(name = "CUSTOMERNAME")
    @NotNull
    private String CustomerName;

    @Column(name = "COFFEENAME")
    @NotNull
    private String CoffeeName;

    @Column(name = "QUANTITY")
    @NotNull
    private Integer quantity;

    @Column(name = "CUSTOMERSUGESSTION")
    private String CustomerSuggestion;
}
