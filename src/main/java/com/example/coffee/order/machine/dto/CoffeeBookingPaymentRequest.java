package com.example.coffee.order.machine.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Setter;

@Builder
@AllArgsConstructor
public class CoffeeBookingPaymentRequest {
    private final String bookingId;
    private final Double amount;
    private final CardDetails cardDetails;
    @Setter
    private boolean fraudAlert = false;

    public String getBookingId() {
        return bookingId;
    }

    public Double getAmount() {
        return amount;
    }

    public CardDetails getCardDetails() {
        return cardDetails;
    }

    public boolean isFraudAlert() {
        return fraudAlert;
    }

}