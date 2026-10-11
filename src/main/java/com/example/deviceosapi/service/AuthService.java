package com.example.deviceosapi.service;

public interface AuthService{
    /**
     * 用户注册
     * @param  phone 手机号
     * @param  password 密码
     * */
    void register(String phone, String password);
}
