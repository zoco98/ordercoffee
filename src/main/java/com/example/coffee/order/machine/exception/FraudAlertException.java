package com.example.coffee.order.machine.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.StandardException;

@StandardException
@Getter
@Setter
@AllArgsConstructor
public class FraudAlertException extends RuntimeException{
    String errorCode;
    String errorMsg;
}
