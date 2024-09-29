package com.example.coffee.order.machine.gateway;


import java.time.LocalDate;

import com.example.coffee.order.machine.dto.FraudCheckResponse;
import com.example.coffee.order.machine.dto.PaymentProcessorResponse;
import com.example.coffee.order.machine.dto.PaymentProcessorResponseRequest;
import org.springframework.stereotype.Component;

@Component
public class PaymentProcessorGateway {

    private final PaymentProcessorRestTemplate restTemplate = new PaymentProcessorRestTemplate();
    private String baseUrl;
    /*private final String host = "localhost";
    private final String port = "8081";*/

    public PaymentProcessorGateway(final String host, final int port) {
        this.baseUrl = this.baseUrl = "http://" + host + ":" + port;
    }

    public PaymentProcessorResponse makePayment(String creditCardNumber, LocalDate creditCardExpiry, Double amount) {
        final PaymentProcessorResponseRequest request = new PaymentProcessorResponseRequest(creditCardNumber, creditCardExpiry, amount);
        return restTemplate.postForObject(baseUrl + "/payments", request, PaymentProcessorResponse.class);
    }

    public void updatePayment(String bookingId) {
        restTemplate.postForObject(baseUrl + "/update", bookingId, Void.class);
    }

    public FraudCheckResponse fraudCheck(String cardNumber) {
        return restTemplate.getForObject(baseUrl + "/fraudCheck/"+cardNumber, FraudCheckResponse.class);
    }
}
