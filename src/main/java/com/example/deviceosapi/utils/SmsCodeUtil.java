package com.example.deviceosapi.utils;

import java.util.Random;

/**
 * 用于生成验证码数据
 * */
public class SmsCodeUtil {
    //生成验证码
    public static String generateCode() {
        Random random = new Random();
        int code = random.nextInt(900000)+100000;
        return String.valueOf(code);
    }
}
