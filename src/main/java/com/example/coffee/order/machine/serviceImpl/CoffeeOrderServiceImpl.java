package com.example.coffee.order.machine.serviceImpl;

import com.example.coffee.order.machine.Repository.CoffeeRepository;
import com.example.coffee.order.machine.Repository.OrderRepository;
import com.example.coffee.order.machine.dto.CardDetails;
import com.example.coffee.order.machine.dto.CoffeeBookingPaymentRequest;
import com.example.coffee.order.machine.dto.CoffeeBookingResponse;
import com.example.coffee.order.machine.entity.Coffee;
import com.example.coffee.order.machine.entity.Order;
import com.example.coffee.order.machine.exception.FraudAlertException;
import com.example.coffee.order.machine.mapper.CoffeeBookingPaymentRequestMapper;
import com.example.coffee.order.machine.service.CoffeeOrderService;
import com.example.coffee.order.machine.service.PaymentGatewayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.coffee.order.machine.util.RandomNumberGenerator.generateErrorCode;
import static com.example.coffee.order.machine.util.RandomNumberGenerator.generateInvoiceNumber;

@Service
public class CoffeeOrderServiceImpl implements CoffeeOrderService {
    @Autowired
    private CoffeeRepository coffeeRepo;
    @Autowired
    private OrderRepository orderRepo;
    @Autowired
    private PaymentGatewayService paymentGatewayService;
    @Override
    public List<Coffee> getCoffeeList() {
        return coffeeRepo.findAll();
    }

    @Transactional
    @Override
    public ResponseEntity<Order> orderCoffee(String customerName, String coffeeName, int quantity, CardDetails cardDetails) throws FraudAlertException {
        Coffee coffeeDetails = coffeeRepo.getReferenceById(coffeeName);
        if(!isCoffeeOutOfStock(coffeeDetails)){
            Order order = Order.builder()
                    .orderId(generateInvoiceNumber(5))
                    .CustomerName(customerName)
                    .CoffeeName(coffeeDetails.getCoffeeName())
                    .quantity(quantity)
                    .CustomerSuggestion(coffeeDetails.getCustomerSuggestion()).build();
            CoffeeBookingPaymentRequest coffeeBookingPaymentRequest = CoffeeBookingPaymentRequestMapper.mapCoffeeBookingPaymentRequest(order, cardDetails);
            CoffeeBookingResponse coffeeBookingResponse = paymentGatewayService.makePaymentAndBookYourCoffee(coffeeBookingPaymentRequest);
            if(coffeeBookingPaymentRequest.isFraudAlert()){
                String errorMessage = "Your Card "+coffeeBookingPaymentRequest.getCardDetails().getNumber()+" is not valid and rejecting payment";
                throw new FraudAlertException(generateErrorCode(), errorMessage);
            }
            if(coffeeBookingResponse.getBookingResponseStatus().equals(CoffeeBookingResponse.BookingResponseStatus.SUCCESS)) {
                coffeeRepo.updateCoffeeStock(quantity, coffeeDetails.getCoffeeName());
                orderRepo.save(order);
                return new ResponseEntity<>(order, HttpStatus.OK);
            }else{
                paymentGatewayService.batchPaymentByBookingId(coffeeBookingResponse.getBookingId(), coffeeBookingResponse.getPaymentId());
            }
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
