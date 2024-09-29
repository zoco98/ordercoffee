package com.example.coffee.order.machine.dto;


public class CoffeeBookingResponse {
    private final String bookingId;
    private final String paymentId;
    private final BookingResponseStatus bookingResponseStatus;

    public CoffeeBookingResponse(String bookingId, String paymentId, BookingResponseStatus bookingResponseStatus) {
        this.bookingId = bookingId;
        this.paymentId = paymentId;
        this.bookingResponseStatus = bookingResponseStatus;
    }

    public enum BookingResponseStatus {
        SUCCESS, REJECTED
    }

    public String getBookingId() {
        return bookingId;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public BookingResponseStatus getBookingResponseStatus() {
        return bookingResponseStatus;
    }


}
