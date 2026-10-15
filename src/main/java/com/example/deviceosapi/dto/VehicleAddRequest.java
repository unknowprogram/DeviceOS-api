package com.example.deviceosapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class VehicleAddRequest {

    @NotBlank(message = "VIN不能为空")
    @Size(min = 17, max = 17, message = "VIN长度必须为17")
    private String vin;

    @NotBlank(message = "车辆品牌不能为空")
    private String brand;

    @NotBlank(message = "车辆型号不能为空")
    private String model;

    @NotBlank(message = "车牌号不能为空")
    @Size(min = 7, max = 7, message = "车牌号长度必须为7")
    private String plateNumber;
}
