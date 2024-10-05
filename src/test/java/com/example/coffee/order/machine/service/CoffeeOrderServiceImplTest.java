package com.example.coffee.order.machine.service;

import com.example.coffee.order.machine.serviceImpl.CoffeeOrderServiceImpl;
import org.junit.jupiter.api.BeforeEach;

public class CoffeeOrderServiceImplTest {

    private CoffeeOrderServiceImpl coffeeOrderService;
    @BeforeEach
    void setup(){
        //PaymentProcessorGateway paymentProcessorGateway = new PaymentProcessorGateway();
        coffeeOrderService = new CoffeeOrderServiceImpl();
    }
}
