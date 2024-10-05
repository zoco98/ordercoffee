package com.example.coffee.order.machine.controlleradvice;


import com.example.coffee.order.machine.exception.ErrorCodeResponse;
import com.example.coffee.order.machine.exception.FraudAlertException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(FraudAlertException.class)
    public ResponseEntity<ErrorCodeResponse> handleFraudAlertException(FraudAlertException fraudAlertException){
        ErrorCodeResponse errorCodeResponse = new ErrorCodeResponse(HttpStatus.BAD_REQUEST.value(), fraudAlertException.getErrorCode(),fraudAlertException.getErrorMsg());
        log.debug("Card number is invalid: {}{}",fraudAlertException.getErrorCode(),fraudAlertException.getErrorMsg());
        return new ResponseEntity<>(errorCodeResponse, HttpStatus.BAD_REQUEST);
    }

}
