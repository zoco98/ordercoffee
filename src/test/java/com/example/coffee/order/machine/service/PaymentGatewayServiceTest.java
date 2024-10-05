package com.example.coffee.order.machine.service;

import com.example.coffee.order.machine.dto.CardDetails;
import com.example.coffee.order.machine.dto.CoffeeBookingPaymentRequest;
import com.example.coffee.order.machine.dto.CoffeeBookingResponse;
import com.example.coffee.order.machine.gateway.PaymentProcessorGateway;
import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.AfterEach;
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
    //Single stubbing
    @Test
    public void batchPayment_SuccessResponse(){
        String bookingID = "d3qJp";
        String paymentID = "ba4e8007-26ac-4e93-8786-048344857411";
        stubFor(any(anyUrl()).willReturn(ok()));
        paymentGatewayService.batchPaymentByBookingId(bookingID, paymentID);
        verify(1, postRequestedFor(urlEqualTo("/update")));
    }

    //Mutiple stubbing with json request body
    @Test
    public void makePaymentAndBookYourCoffee_SuccessResponse(){
        final CardDetails cardDetails = CardDetails.builder()
                .number("111187945126")
                .expiry(LocalDate.of(2029,05,01)).build();
        CoffeeBookingPaymentRequest coffeetBookingPaymentRequest = CoffeeBookingPaymentRequest.builder()
                .bookingId("orderid1")
                .amount(100.0)
                .cardDetails(cardDetails).build();
        stubFor(post(urlPathEqualTo("/payments"))
                .withRequestBody(equalToJson("{\n" +
                        "    \"cardNumber\":\"111187945126\",\n" +
                        "    \"cardExpiryDate\": \"2029-05-01\",\n" +
                        "    \"amount\": 100.0\n" +
                        "}"))
                .willReturn(okJson("{\n" +
                        "    \"paymentId\": \"ba4e8007-26ac-4e93-8786-048344857411\",\n" +
                        "    \"paymentResponseStatus\": \"SUCCESS\"\n" +
                        "}")));
        stubFor(get(urlPathMatching("/fraudCheck/.*"))
                .willReturn(okJson("{\n" +
                        "    \"blacklisted\": false\n" +
                        "}")));
        CoffeeBookingResponse coffeeBookingResponse = paymentGatewayService.makePaymentAndBookYourCoffee(coffeetBookingPaymentRequest);
        verify(1, postRequestedFor(urlEqualTo("/payments"))
                .withRequestBody(equalToJson("{\n" +
                "    \"cardNumber\":\"111187945126\",\n" +
                "    \"cardExpiryDate\": \"2029-05-01\",\n" +
                "    \"amount\": 100.0\n" +
                "}")));
        assertEquals(coffeeBookingResponse.getBookingResponseStatus(), CoffeeBookingResponse.BookingResponseStatus.SUCCESS);
    }

    //Multiple Stubbing without json request body
    @Test
    public void makePaymentAndBookYourCoffee_FraudCheck_ErrorResponse(){
        final CardDetails cardDetails = CardDetails.builder()
                .number("110087945126")
                .expiry(LocalDate.of(2029,05,01)).build();
        CoffeeBookingPaymentRequest coffeetBookingPaymentRequest = CoffeeBookingPaymentRequest.builder()
                .bookingId("orderid1")
                .amount(100.0)
                .cardDetails(cardDetails)
                .fraudAlert(true).build();

        stubFor(post(urlPathEqualTo("/payments"))
                .withRequestBody(matchingJsonPath("cardNumber"))
                .withRequestBody(matchingJsonPath("cardExpiryDate"))
                .withRequestBody(matchingJsonPath("amount"))
                .willReturn(okJson("{\n" +
                        "    \"paymentId\": \"8d282192-a351-4761-a50b-605448bfac92\",\n" +
                        "    \"paymentResponseStatus\": \"FAILED\"\n" +
                        "}")));

        stubFor(get(urlPathMatching("/fraudCheck/.*"))
                .willReturn(okJson("{\n" +
                        "    \"blacklisted\": true\n" +
                        "}")));

        CoffeeBookingResponse coffeeBookingResponse = paymentGatewayService.makePaymentAndBookYourCoffee(coffeetBookingPaymentRequest);
        verify(1, getRequestedFor(urlEqualTo("/fraudCheck/110087945126")));
        assertEquals(coffeeBookingResponse.getBookingResponseStatus(), CoffeeBookingResponse.BookingResponseStatus.REJECTED);
    }

    @AfterEach
    void teardown(){
        wireMockServer.stop();
    }
}
