package com.example.coffee.order.machine.serviceImpl;

import com.example.coffee.order.machine.Repository.CoffeeRepository;
import com.example.coffee.order.machine.Repository.OrderRepository;
import com.example.coffee.order.machine.entity.Coffee;
import com.example.coffee.order.machine.entity.Order;
import com.example.coffee.order.machine.service.CoffeeOrderService;
import com.example.coffee.order.machine.util.InvoiceNumberGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.coffee.order.machine.util.InvoiceNumberGenerator.generateInvoiceNumber;

@Service
public class CoffeeOrderServiceImpl implements CoffeeOrderService {
    @Autowired
    private CoffeeRepository coffeeRepo;
    @Autowired
    private OrderRepository orderRepo;
    @Override
    public List<Coffee> getCoffeeList() {
        return coffeeRepo.findAll();
    }

    @Transactional
    @Override
    public ResponseEntity<Order> orderCoffee(String customerName, String coffeeName, int quantity) {
        Coffee coffeeDetails = coffeeRepo.getReferenceById(coffeeName);
        if(!isCoffeeOutOfStock(coffeeDetails)){
            Order order = Order.builder()
                    .orderId(generateInvoiceNumber(5))
                    .CustomerName(customerName)
                    .CoffeeName(coffeeDetails.getCoffeeName())
                    .quantity(quantity)
                    .CustomerSuggestion(coffeeDetails.getCustomerSuggestion()).build();
            orderRepo.save(order);
            coffeeRepo.updateCoffeeStock(quantity, coffeeDetails.getCoffeeName());

            return new ResponseEntity<>(order, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @Override
    public List<Order> getAllOrder() {
        return orderRepo.findAll();
    }

    @Override
    public List<Order> getOrderByInvoiceNumber(String invoiceNumber) {
        List<Order> orderList = getAllOrder().stream().filter(o -> o.getOrderId().equalsIgnoreCase(invoiceNumber)).toList();
        return orderList;
    }

    private static boolean isCoffeeOutOfStock(Coffee coffeeDetails) {
        return (coffeeDetails!=null && coffeeDetails.getCoffeeStock() <= 0);
    }
}
