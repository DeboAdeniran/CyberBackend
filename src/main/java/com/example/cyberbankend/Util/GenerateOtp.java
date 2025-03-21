package com.example.cyberbankend.Util;

import java.security.SecureRandom;
import java.util.Random;

public class GenerateOtp {
    private static final SecureRandom random = new SecureRandom();

    public static String generateOTP(){
        int randomNum = 100000 + random.nextInt(900000);

        return String.valueOf(randomNum);
    }
}
