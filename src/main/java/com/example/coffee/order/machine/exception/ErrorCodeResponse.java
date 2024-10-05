package com.example.coffee.order.machine.exception;

import lombok.*;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ErrorCodeResponse {

    private int statusCode;
    private String errorCode;
    private String errorMsg;
}
