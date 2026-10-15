package com.example.deviceosapi.service;

import com.example.deviceosapi.dto.LoginResponse;

public interface AuthService{
    /**
     * 用户注册
     * @param  phone 手机号
     * @param  password 密码
     * */
    void register(String phone,String code, String password);

    /**
     * 生成验证码
     * */
    String generateCode(String phone);

    /**
     * 登录
     * */
    LoginResponse login(String phone, String password);
}
