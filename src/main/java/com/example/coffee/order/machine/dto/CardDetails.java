package com.example.coffee.order.machine.dto;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public class CardDetails {
    private final String number;
    private final LocalDate expiry;

    public CardDetails(String number, LocalDate expiry) {
        this.number = number;
        this.expiry = expiry;
    }

    public String getNumber() {
        return number;
    }

    public LocalDate getExpiry() {
        return expiry;
    }
}
