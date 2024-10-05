package com.example.coffee.order.machine.service;

import com.example.coffee.order.machine.dto.*;
import com.example.coffee.order.machine.gateway.PaymentProcessorGateway;
import org.springframework.stereotype.Component;

@Component
public class PaymentGatewayService {

    private PaymentProcessorGateway paymentProcessorGateway;

    public PaymentGatewayService(PaymentProcessorGateway paymentProcessorGateway) {
        this.paymentProcessorGateway = paymentProcessorGateway;
    }

    public CoffeeBookingResponse makePaymentAndBookYourCoffee(CoffeeBookingPaymentRequest coffeetBookingPaymentRequest) {
        final CardDetails cardDetails = coffeetBookingPaymentRequest.getCardDetails();
        final FraudCheckResponse fraudCheckResponse = paymentProcessorGateway.fraudCheck(cardDetails.getNumber());
            if (fraudCheckResponse.isBlacklisted()) {
                coffeetBookingPaymentRequest.setFraudAlert(true);
                return new CoffeeBookingResponse(coffeetBookingPaymentRequest.getBookingId(), null, CoffeeBookingResponse.BookingResponseStatus.REJECTED);
            }
        final PaymentProcessorResponse paymentProcessorResponse = paymentProcessorGateway.makePayment(cardDetails.getNumber(), cardDetails.getExpiry(), coffeetBookingPaymentRequest.getAmount());

        if (paymentProcessorResponse.getPaymentResponseStatus() == PaymentProcessorResponse.PaymentResponseStatus.SUCCESS) {
            return new CoffeeBookingResponse(coffeetBookingPaymentRequest.getBookingId(), paymentProcessorResponse.getPaymentId(), CoffeeBookingResponse.BookingResponseStatus.SUCCESS);
        }else {
            return new CoffeeBookingResponse(coffeetBookingPaymentRequest.getBookingId(), paymentProcessorResponse.getPaymentId(), CoffeeBookingResponse.BookingResponseStatus.REJECTED);
        }
    }

    public CoffeeBookingResponse batchPaymentByBookingId(String bookingId, String paymentId){
        paymentProcessorGateway.updatePayment(bookingId);
        return new CoffeeBookingResponse(bookingId, paymentId, CoffeeBookingResponse.BookingResponseStatus.SUCCESS);
    }
}
