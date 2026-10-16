package com.example.deviceosapi.dto.response;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 车辆归属核验请求
 * */
@Data
public class VehicleVerificationRequest {

    @NotBlank(message = "车牌号不能为空")
    private String plateNumber;
}
