package com.example.deviceosapi.vo.request;

import lombok.Data;

@Data
public class VehicleSearchRequest {
    private Long id;

    private String brand;

    private String model;

    private String plateNumber;
}
