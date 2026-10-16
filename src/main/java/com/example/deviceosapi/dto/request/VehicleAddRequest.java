package com.example.deviceosapi.dto.request;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;

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

    /** 以下为可选的初始状态，不填就不创建遥测记录 */

    @DecimalMin(value = "0", message = "累计里程不能为负")
    private BigDecimal mileageKm;

    @DecimalMin(value = "0", message = "电量百分比不能小于0")
    @DecimalMax(value = "100", message = "电量百分比不能大于100")
    private BigDecimal batteryLevel;

    @DecimalMin(value = "0", message = "剩余续航不能为负")
    private BigDecimal remainingRangeKm;

    @DecimalMin(value = "0", message = "电池健康度不能小于0")
    @DecimalMax(value = "100", message = "电池健康度不能大于100")
    private BigDecimal batteryHealth;

    @DecimalMin(value = "0", message = "标称续航不能为负")
    private BigDecimal ratedRangeKm;

    /** PARKED 停车 / DRIVING 行驶 / CHARGING 充电 */
    @Pattern(regexp = "PARKED|DRIVING|CHARGING", message = "车辆状态只能是 PARKED、DRIVING、CHARGING")
    private String drivingStatus;
}
