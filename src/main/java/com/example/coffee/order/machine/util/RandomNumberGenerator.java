package com.example.coffee.order.machine.util;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.UUID;

@Component
public class RandomNumberGenerator {
    private static final SecureRandom random = new SecureRandom();

    public static String generateInvoiceNumber(int length){
        String characterSet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder(length);
        for(int i = 0; i < length; i++){
            int index = random.nextInt(characterSet.length());
            sb.append(characterSet.charAt(index));
        }
        return sb.toString();
    }
    public static String generateErrorCode(){
        return UUID.randomUUID().toString();
    }
}
