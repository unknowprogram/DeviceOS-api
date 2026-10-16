package com.example.deviceosapi.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 车辆绑定请求
 * */
@Data
public class VehicleBindRequest {

    @NotBlank(message = "车牌号不能为空")
    private String plateNumber;
}
