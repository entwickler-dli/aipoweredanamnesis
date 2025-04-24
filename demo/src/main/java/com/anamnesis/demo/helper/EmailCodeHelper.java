package com.anamnesis.demo.helper;

import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Random;

@Component
public class EmailCodeHelper {
    public int generateEmailCode(){
        return 100000 + new Random().nextInt(900000);
    }

    public String encodeEmail(String email) {
        return Base64.getEncoder().encodeToString(email.getBytes(StandardCharsets.UTF_8));
    }

    public String decodeEmail(String encodedEmail) {
        byte[] decodedBytes = Base64.getDecoder().decode(encodedEmail);
        return new String(decodedBytes, StandardCharsets.UTF_8);
    }
}

