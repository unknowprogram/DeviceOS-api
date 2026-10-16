package com.example.deviceosapi.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

@Mapper
public interface VehicleTelemetryMapper {

    // 写入车辆当前状态
    @Insert("""
            INSERT INTO dos_vehicle_telemetry
                (vehicle_id, mileage_km, battery_level, remaining_range_km,
                 battery_health, rated_range_km, driving_status)
            VALUES
                (#{vehicleId}, #{mileageKm}, #{batteryLevel}, #{remainingRangeKm},
                 #{batteryHealth}, #{ratedRangeKm}, #{drivingStatus})
            """)
    int insertTelemetry(@Param("vehicleId") Long vehicleId,
                        @Param("mileageKm") BigDecimal mileageKm,
                        @Param("batteryLevel") BigDecimal batteryLevel,
                        @Param("remainingRangeKm") BigDecimal remainingRangeKm,
                        @Param("batteryHealth") BigDecimal batteryHealth,
                        @Param("ratedRangeKm") BigDecimal ratedRangeKm,
                        @Param("drivingStatus") String drivingStatus);

    // 追加一条历史流水
    @Insert("""
            INSERT INTO dos_vehicle_telemetry_history
                (vehicle_id, mileage_km, battery_level, remaining_range_km,
                 battery_health, driving_status)
            VALUES
                (#{vehicleId}, #{mileageKm}, #{batteryLevel}, #{remainingRangeKm},
                 #{batteryHealth}, #{drivingStatus})
            """)
    int insertTelemetryHistory(@Param("vehicleId") Long vehicleId,
                               @Param("mileageKm") BigDecimal mileageKm,
                               @Param("batteryLevel") BigDecimal batteryLevel,
                               @Param("remainingRangeKm") BigDecimal remainingRangeKm,
                               @Param("batteryHealth") BigDecimal batteryHealth,
                               @Param("drivingStatus") String drivingStatus);
}
