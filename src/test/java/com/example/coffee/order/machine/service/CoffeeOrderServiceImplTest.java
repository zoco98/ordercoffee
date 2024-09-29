package com.example.coffee.order.machine.service;

import com.example.coffee.order.machine.gateway.PaymentProcessorGateway;
import com.example.coffee.order.machine.serviceImpl.CoffeeOrderServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CoffeeOrderServiceImplTest {

    private CoffeeOrderServiceImpl coffeeOrderService;
    @BeforeEach
    void setup(){
        //PaymentProcessorGateway paymentProcessorGateway = new PaymentProcessorGateway();
        coffeeOrderService = new CoffeeOrderServiceImpl();
    }
    @Test
    public void orderCoffeeTest_SuccessResponse(){
        coffeeOrderService.orderCoffee("Jane Doe","Espresso",1);
    }
}
