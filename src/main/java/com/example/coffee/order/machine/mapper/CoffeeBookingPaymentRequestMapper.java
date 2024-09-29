package com.example.coffee.order.machine.mapper;

import com.example.coffee.order.machine.dto.CardDetails;
import com.example.coffee.order.machine.dto.CoffeeBookingPaymentRequest;
import com.example.coffee.order.machine.entity.Order;

import java.time.LocalDate;

public class CoffeeBookingPaymentRequestMapper {

    public static CoffeeBookingPaymentRequest mapCoffeeBookingPaymentRequest(Order order){

        final CardDetails cardDetails = CardDetails.builder()
                .number("111187945126")
                .expiry(LocalDate.ofYearDay(2029,30)).build();
        CoffeeBookingPaymentRequest coffeetBookingPaymentRequest = CoffeeBookingPaymentRequest.builder()
                .bookingId(order.getOrderId())
                .amount(Double.valueOf(order.getQuantity()*100))
                .cardDetails(cardDetails).build();
        return coffeetBookingPaymentRequest;
    }
}
