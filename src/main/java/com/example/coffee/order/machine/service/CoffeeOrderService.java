package com.example.coffee.order.machine.service;

import com.example.coffee.order.machine.dto.CardDetails;
import com.example.coffee.order.machine.entity.Coffee;
import com.example.coffee.order.machine.entity.Order;
import com.example.coffee.order.machine.exception.FraudAlertException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

public interface CoffeeOrderService {


    List<Coffee> getCoffeeList();

    ResponseEntity<Order> orderCoffee(String customerName, String coffeeName, int quantity, CardDetails cardDetails) throws FraudAlertException;

    List<Order> getAllOrder();

    List<Order> getOrderByInvoiceNumber(String invoiceNumber);
}
