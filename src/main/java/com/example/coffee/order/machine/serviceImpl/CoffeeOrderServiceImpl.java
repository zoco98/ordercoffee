package com.example.coffee.order.machine.serviceImpl;

import com.example.coffee.order.machine.Repository.CoffeeRepository;
import com.example.coffee.order.machine.Repository.OrderRepository;
import com.example.coffee.order.machine.dto.*;
import com.example.coffee.order.machine.entity.Coffee;
import com.example.coffee.order.machine.entity.Order;
import com.example.coffee.order.machine.gateway.PaymentProcessorGateway;
import com.example.coffee.order.machine.service.CoffeeOrderService;
import com.example.coffee.order.machine.util.InvoiceNumberGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static com.example.coffee.order.machine.util.InvoiceNumberGenerator.generateInvoiceNumber;

@Service
public class CoffeeOrderServiceImpl implements CoffeeOrderService {
    @Autowired
    private CoffeeRepository coffeeRepo;
    @Autowired
    private OrderRepository orderRepo;
    @Autowired
    private PaymentProcessorGateway paymentProcessorGateway;
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
            coffeeRepo.updateCoffeeStock(quantity, coffeeDetails.getCoffeeName());
            final CardDetails cardDetails = CardDetails.builder()
                    .number("111187945126")
                    .expiry(LocalDate.ofYearDay(2029,30)).build();
            CoffeetBookingPaymentRequest coffeetBookingPaymentRequest = CoffeetBookingPaymentRequest.builder()
                    .bookingId(order.getOrderId())
                    .amount(Double.valueOf(order.getQuantity()*100))
                    .cardDetails(cardDetails).build();
            CoffeeBookingResponse coffeeBookingResponse = makePaymentAndBookYourCoffee(coffeetBookingPaymentRequest);
            if(coffeeBookingResponse!=null) {
                orderRepo.save(order);
            }

            return new ResponseEntity<>(order, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    private CoffeeBookingResponse makePaymentAndBookYourCoffee(CoffeetBookingPaymentRequest coffeetBookingPaymentRequest) {
        final CardDetails cardDetails = coffeetBookingPaymentRequest.getCardDetails();

        if (coffeetBookingPaymentRequest.isFraudAlert()) {
            final FraudCheckResponse fraudCheckResponse = paymentProcessorGateway.fraudCheck(cardDetails.getNumber());
            if (fraudCheckResponse.isBlacklisted()) {
                return new CoffeeBookingResponse(coffeetBookingPaymentRequest.getBookingId(), null, CoffeeBookingResponse.BookingResponseStatus.REJECTED);
            }
        }
        final PaymentProcessorResponse paymentProcessorResponse = paymentProcessorGateway.makePayment(cardDetails.getNumber(), cardDetails.getExpiry(), coffeetBookingPaymentRequest.getAmount());

        if (paymentProcessorResponse.getPaymentResponseStatus() == PaymentProcessorResponse.PaymentResponseStatus.SUCCESS) {
            return new CoffeeBookingResponse(coffeetBookingPaymentRequest.getBookingId(), paymentProcessorResponse.getPaymentId(), CoffeeBookingResponse.BookingResponseStatus.SUCCESS);
        } else {
            return new CoffeeBookingResponse(coffeetBookingPaymentRequest.getBookingId(), paymentProcessorResponse.getPaymentId(), CoffeeBookingResponse.BookingResponseStatus.REJECTED);
        }
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
