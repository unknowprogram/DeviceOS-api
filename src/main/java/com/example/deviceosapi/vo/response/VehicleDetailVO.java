package com.example.deviceosapi.vo.response;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class VehicleDetailVO {
    /** 车辆ID */
    private Long id;

    /** 车辆识别码（已脱敏，前3后4） */
    private String vin;

    /** 品牌 */
    private String brand;

    /** 型号 */
    private String model;

    /** 车牌号 */
    private String plateNumber;

    /** 车辆状态：ACTIVE / DISABLED */
    private String status;

    /** 电量百分比 */
    private BigDecimal batteryLevel;

    /** 剩余续航（km） */
    private BigDecimal remainingRangeKm;

    /** 满电标称续航（km） */
    private BigDecimal ratedRangeKm;

    /** 电池健康度（%） */
    private BigDecimal batteryHealth;

    /** 累计里程（km） */
    private BigDecimal mileageKm;

    /** 车辆状态：PARKED 停车 / DRIVING 行驶 / CHARGING 充电 */
    private String drivingStatus;

    /** 状态数据更新时间，前端用来显示"x 分钟前更新" */
    private LocalDateTime statusUpdatedAt;

    /** 当前用户与车辆的关系：OWNER 车主 / DRIVER 用车人 */
    private String role;

    /** 绑定时间 */
    private LocalDateTime boundAt;
}
