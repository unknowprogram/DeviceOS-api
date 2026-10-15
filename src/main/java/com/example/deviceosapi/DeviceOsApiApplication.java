package com.example.deviceosapi;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.deviceosapi.mapper")
public class DeviceOsApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(DeviceOsApiApplication.class, args);
    }

}
