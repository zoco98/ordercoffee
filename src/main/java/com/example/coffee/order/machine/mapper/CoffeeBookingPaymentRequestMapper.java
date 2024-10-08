package com.example.coffee.order.machine.mapper;

import com.example.coffee.order.machine.dto.CardDetails;
import com.example.coffee.order.machine.dto.CoffeeBookingPaymentRequest;
import com.example.coffee.order.machine.entity.Order;

import java.time.LocalDate;

public class CoffeeBookingPaymentRequestMapper {

    public static CoffeeBookingPaymentRequest mapCoffeeBookingPaymentRequest(Order order, CardDetails cardDetails){

        CoffeeBookingPaymentRequest coffeetBookingPaymentRequest = CoffeeBookingPaymentRequest.builder()
                .bookingId(order.getOrderId())
                .amount(Double.valueOf(order.getQuantity()*100))
                .cardDetails(cardDetails).build();
        return coffeetBookingPaymentRequest;
    }
}
