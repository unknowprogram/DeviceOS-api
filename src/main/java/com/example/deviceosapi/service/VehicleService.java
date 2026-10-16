package com.example.deviceosapi.service;

import com.example.deviceosapi.dto.request.VehicleAddRequest;
import com.example.deviceosapi.model.Vehicle;
import com.example.deviceosapi.vo.response.VehicleDetailVO;
import com.example.deviceosapi.vo.response.VehicleSearchVO;

import java.util.List;

public interface VehicleService {
    /**新增车辆信息（可带初始里程/电量等状态）*/
    Vehicle addVehicleInfo(VehicleAddRequest request);

    /**
     * 根据车牌号绑定车辆
     *
     * @param userId 当前登录用户ID
     * @param plateNumber 车牌号
     */
    void bindVehicle(String plateNumber, Long userId);

    /**
     * 查询车辆信息
     * */
    List<VehicleSearchVO> searchVehicles(String plateNumber);

    /**
     * 提交车辆归属核验
     *
     * @param plateNumber 车牌号
     * @param userId 当前登录用户ID
     */
    void submitVehicleVerification(String plateNumber, Long userId);

    /**
     * 查询当前用户已绑定的车辆
     *
     * @param userId 当前登录用户ID
     */
    List<VehicleSearchVO> myVehicles(Long userId);

    /**
     * 查询车辆详情（首页展示）
     *
     * @param vehicleId 车辆ID
     * @param userId 当前登录用户ID
     */
    VehicleDetailVO getVehicleDetail(Long vehicleId, Long userId);
}
