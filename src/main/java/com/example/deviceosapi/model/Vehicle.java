package com.example.deviceosapi.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("dos_vehicle")
public class Vehicle {
    @TableId(type = IdType.AUTO)
    private Long id;
    /** 车辆识别码 */
    private String vin;

    /** 车辆品牌 */
    private String brand;

    /** 车辆型号 */
    private String model;

    /** 车牌号 */
    private String plateNumber;

    /** 车辆状态 */
    private String status;

    /** 当前车主用户ID */
    private Long ownerUserId;

    /** 创建时间 */
    private LocalDateTime createdAt;

    /** 更新时间 */
    private LocalDateTime updatedAt;
}
