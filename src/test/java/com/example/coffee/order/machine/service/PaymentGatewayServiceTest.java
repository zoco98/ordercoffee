package com.example.coffee.order.machine.service;

import com.example.coffee.order.machine.dto.CardDetails;
import com.example.coffee.order.machine.dto.CoffeeBookingPaymentRequest;
import com.example.coffee.order.machine.dto.CoffeeBookingResponse;
import com.example.coffee.order.machine.gateway.PaymentProcessorGateway;
import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class PaymentGatewayServiceTest {
    private PaymentGatewayService paymentGatewayService;
    WireMockServer wireMockServer = new WireMockServer();


    @BeforeEach
    void setup(){
        configureFor("localhost",8080);
        wireMockServer.start();
        PaymentProcessorGateway paymentProcessorGateway = new PaymentProcessorGateway("localhost", wireMockServer.port());
        paymentGatewayService = new PaymentGatewayService(paymentProcessorGateway);
    }

    @Test
    public void makePaymentAndBookYourCoffee_SuccessResponse(){
        final CardDetails cardDetails = CardDetails.builder()
                .number("111187945126")
                .expiry(LocalDate.of(2029,05,01)).build();
        CoffeeBookingPaymentRequest coffeetBookingPaymentRequest = CoffeeBookingPaymentRequest.builder()
                .bookingId("orderid1")
                .amount(null)
                .cardDetails(cardDetails).build();
        stubFor(post(urlPathEqualTo("/payments"))
                .withRequestBody(equalToJson("{\n" +
                        "    \"cardNumber\":\"111187945126\",\n" +
                        "    \"cardExpiryDate\": \"2029-05-01\",\n" +
                        "    \"amount\": null\n" +
                        "}"))
                .willReturn(okJson("{\n" +
                        "    \"paymentId\": \"ba4e8007-26ac-4e93-8786-048344857411\",\n" +
                        "    \"paymentResponseStatus\": \"SUCCESS\"\n" +
                        "}")));

        CoffeeBookingResponse coffeeBookingResponse = paymentGatewayService.makePaymentAndBookYourCoffee(coffeetBookingPaymentRequest);
        assertEquals(coffeeBookingResponse.getBookingResponseStatus(), CoffeeBookingResponse.BookingResponseStatus.SUCCESS);
    }
    @AfterEach
    void teardown(){
        wireMockServer.stop();
    }
}
