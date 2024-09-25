package com.example.coffee.order.machine.controller;

import com.example.coffee.order.machine.entity.Coffee;
import com.example.coffee.order.machine.entity.Order;
import com.example.coffee.order.machine.service.CoffeeOrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/coffee")
public class CoffeeOrderController {

    @Autowired
    private CoffeeOrderService coffeeOrderService;

    /*curl --location 'http://localhost:8080/coffee/' \
    --header 'Authorization: Basic cm9vdDo5ODIwNDFAU3M='
    * */
    @GetMapping("/")
    public List<Coffee> getCoffeeList(){
        List<Coffee> coffeeList = coffeeOrderService.getCoffeeList();
        return coffeeList;
    }

    /*curl --location 'http://localhost:8080/coffee/order/johndoe?coffeeName=espresso&quantity=2' \
--header 'Authorization: Basic cm9vdDo5ODIwNDFAU3M='
    * */
    @PostMapping("/order/{customerName}")
    public ResponseEntity<Order> orderCoffee(
            @PathVariable("customerName") String customerName,
            @Valid @RequestParam("coffeeName") String coffeeName,
            @Valid @RequestParam("quantity") int quantity){
        ResponseEntity<Order> status = coffeeOrderService.orderCoffee(customerName, coffeeName, quantity);
        return status;
    }

    @GetMapping("/order/invoice/{invoiceNumber}")
    public List<Order> getOrderByInvoiceNumber(
            @PathVariable("invoiceNumber") String invoiceNumber){
        List<Order> order = coffeeOrderService.getOrderByInvoiceNumber(invoiceNumber);
        return order;
    }

    @GetMapping("/order")
    public List<Order> getOrders(){
        List<Order> orderList = coffeeOrderService.getAllOrder();
        return orderList;
    }
}
