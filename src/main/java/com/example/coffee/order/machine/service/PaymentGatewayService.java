package com.example.coffee.order.machine.service;

import com.example.coffee.order.machine.dto.*;
import com.example.coffee.order.machine.gateway.PaymentProcessorGateway;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class PaymentGatewayService {

    @Value("${app.host}")
    private String host;

    @Value("${app.port}")
    private int port;
    private PaymentProcessorGateway paymentProcessorGateway = new PaymentProcessorGateway(host, port);

    public PaymentGatewayService(PaymentProcessorGateway paymentProcessorGateway) {
        this.paymentProcessorGateway = paymentProcessorGateway;
    }

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
