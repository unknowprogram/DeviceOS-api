package com.example.deviceosapi.mapper;

import com.example.deviceosapi.vo.response.VehicleDetailVO;
import com.example.deviceosapi.vo.response.VehicleSearchVO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface VehicleBindMapper {

    //    @Select("""
//        SELECT id, status, owner_user_id
//        FROM dos_vehicle
//        WHERE vin = #{vin}
//        FOR UPDATE
//        """)
//    VehicleRow selectVehicleForUpdate(@Param("vin") String vin);
    // 查询车辆并加锁，防止并发绑定
    @Select("""
            SELECT id, status, owner_user_id
            FROM dos_vehicle
            WHERE plate_number = #{plateNumber}
            FOR UPDATE
            """)
    VehicleRow selectVehicleForUpdate(@Param("plateNumber") String plateNumber);

    // 查询当前用户是否已通过该车辆的归属核验
    @Select("""
            SELECT COUNT(*)
            FROM dos_vehicle_verification
            WHERE user_id = #{userId}
              AND vehicle_id = #{vehicleId}
              AND status = 'VERIFIED'
            """)
    int countVerified(@Param("userId") Long userId,
                      @Param("vehicleId") Long vehicleId);

    // 查询是否已经存在有效绑定
    @Select("""
            SELECT COUNT(*)
            FROM dos_user_vehicle
            WHERE user_id = #{userId}
              AND vehicle_id = #{vehicleId}
              AND status = 'ACTIVE'
            """)
    int countActiveBinding(@Param("userId") Long userId,
                           @Param("vehicleId") Long vehicleId);

    // 更新车辆当前所有人
    @Update("""
            UPDATE dos_vehicle
            SET owner_user_id = #{userId}
            WHERE id = #{vehicleId}
            """)
    int updateOwner(@Param("vehicleId") Long vehicleId,
                    @Param("userId") Long userId);

    // 写入用户车辆关联关系
    @Insert("""
            INSERT INTO dos_user_vehicle
                (user_id, vehicle_id, role, status, bound_at)
            VALUES
                (#{userId}, #{vehicleId}, 'OWNER', 'ACTIVE', NOW())
            """)
    int insertBinding(@Param("userId") Long userId,
                      @Param("vehicleId") Long vehicleId);

    //查询车辆信息
    @Select("""
            <script>
            SELECT
                id,
                brand,
                model,
                plate_number AS plateNumber
            FROM dos_vehicle
            WHERE status = 'ACTIVE'
            <if test="plateNumber != null and plateNumber != ''">
                AND plate_number LIKE CONCAT('%', #{plateNumber}, '%')
            </if>
            ORDER BY id DESC
            LIMIT 20
            </script>
            """)
    List<VehicleSearchVO> searchVehicles(@Param("plateNumber") String plateNumber);

    ;

    // 按车牌号查询车辆ID（提交核验用）
    @Select("""
            SELECT id
            FROM dos_vehicle
            WHERE plate_number = #{plateNumber}
              AND status = 'ACTIVE'
            LIMIT 1
            """)
    Long selectVehicleIdByPlateNumber(@Param("plateNumber") String plateNumber);

    // 写入车辆归属核验记录（简化：直接置为已核验）
    @Insert("""
            INSERT INTO dos_vehicle_verification
                (user_id, vehicle_id, status, verification_method, verified_at)
            VALUES
                (#{userId}, #{vehicleId}, 'VERIFIED', 'DEMO', NOW())
            """)
    int insertVerification(@Param("userId") Long userId,
                           @Param("vehicleId") Long vehicleId);

    // 查询用户已绑定的车辆
    @Select("""
            SELECT
                v.id,
                v.brand,
                v.model,
                v.plate_number AS plateNumber
            FROM dos_user_vehicle uv
            JOIN dos_vehicle v ON v.id = uv.vehicle_id
            WHERE uv.user_id = #{userId}
              AND uv.status = 'ACTIVE'
              AND v.status = 'ACTIVE'
            ORDER BY uv.bound_at DESC
            """)
    List<VehicleSearchVO> selectMyVehicles(@Param("userId") Long userId);

    // 查询车辆详情：车辆信息 + 实时状态 + 当前用户的绑定关系
    // 注意用户条件写在 join 里，查不到就说明这辆车不属于当前用户
    @Select("""
            SELECT
                v.id,
                v.vin,
                v.brand,
                v.model,
                v.plate_number       AS plateNumber,
                v.status,
                t.battery_level      AS batteryLevel,
                t.remaining_range_km AS remainingRangeKm,
                t.rated_range_km     AS ratedRangeKm,
                t.battery_health     AS batteryHealth,
                t.mileage_km         AS mileageKm,
                t.driving_status     AS drivingStatus,
                t.updated_at         AS statusUpdatedAt,
                uv.role,
                uv.bound_at          AS boundAt
            FROM dos_vehicle v
            JOIN dos_user_vehicle uv
                 ON uv.vehicle_id = v.id
                AND uv.user_id = #{userId}
                AND uv.status = 'ACTIVE'
            LEFT JOIN dos_vehicle_telemetry t ON t.vehicle_id = v.id
            WHERE v.id = #{vehicleId}
              AND v.status = 'ACTIVE'
            """)
    VehicleDetailVO selectVehicleDetail(@Param("vehicleId") Long vehicleId,
                                        @Param("userId") Long userId);


    // 使用 MyBatis 映射查询结果
    // 字段名必须是驼峰 ownerUserId，mapUnderscoreToCamelCase 才能把 owner_user_id 映射进来
    class VehicleRow {
        public Long id;
        public String status;
        public Long ownerUserId;
    }
}
