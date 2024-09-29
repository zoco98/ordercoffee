package com.example.coffee.order.machine.service;

import com.example.coffee.order.machine.dto.*;
import com.example.coffee.order.machine.gateway.PaymentProcessorGateway;
import org.springframework.beans.factory.annotation.Autowired;

public class PaymentGatewayService {

    @Autowired
    private PaymentProcessorGateway paymentProcessorGateway;

    public CoffeeBookingResponse makePaymentAndBookYourCoffee(CoffeeBookingPaymentRequest coffeetBookingPaymentRequest) {
        final CardDetails cardDetails = coffeetBookingPaymentRequest.getCardDetails();

        if (coffeetBookingPaymentRequest.isFraudAlert()) {
            final FraudCheckResponse fraudCheckResponse = paymentProcessorGateway.fraudCheck(cardDetails.getNumber());
            if (fraudCheckResponse.isBlacklisted()) {
                return new CoffeeBookingResponse(coffeetBookingPaymentRequest.getBookingId(), null, CoffeeBookingResponse.BookingResponseStatus.REJECTED);
            }
        }
        final PaymentProcessorResponse paymentProcessorResponse = paymentProcessorGateway.makePayment(cardDetails.getNumber(), cardDetails.getExpiry(), coffeetBookingPaymentRequest.getAmount());

        if (paymentProcessorResponse.getPaymentResponseStatus() == PaymentProcessorResponse.PaymentResponseStatus.SUCCESS) {
            return new CoffeeBookingResponse(coffeetBookingPaymentRequest.getBookingId(), paymentProcessorResponse.getPaymentId(), CoffeeBookingResponse.BookingResponseStatus.SUCCESS);
        } else {
            return new CoffeeBookingResponse(coffeetBookingPaymentRequest.getBookingId(), paymentProcessorResponse.getPaymentId(), CoffeeBookingResponse.BookingResponseStatus.REJECTED);
        }
    }
}
