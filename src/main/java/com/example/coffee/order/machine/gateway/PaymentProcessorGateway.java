package com.example.coffee.order.machine.gateway;


import java.time.LocalDate;

import com.example.coffee.order.machine.dto.FraudCheckResponse;
import com.example.coffee.order.machine.dto.PaymentProcessorResponse;
import com.example.coffee.order.machine.dto.PaymentProcessorResponseRequest;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class PaymentProcessorGateway {
    @Value("${app.baseurl}")
    private String baseUrl;
    private final PaymentProcessorRestTemplate restTemplate = new PaymentProcessorRestTemplate();

    public PaymentProcessorGateway(final String host, final int port) {
        this.baseUrl = "http://" + host + ":" + port;
    }
    public PaymentProcessorGateway() {
        this.baseUrl = "http://localhost:8081";
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
