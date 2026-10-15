package com.example.deviceosapi.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class VehicleAddRequest {

    @NotBlank(message = "VIN不能为空")
    private String vin;

    @NotBlank(message = "车辆品牌不能为空")
    private String brand;

    @NotBlank(message = "车辆型号不能为空")
    private String model;

    @NotBlank(message = "车牌号不能为空")
    private String plateNumber;
}
