package com.example.deviceosapi.controller;

import com.example.deviceosapi.common.ApiResponse;
import com.example.deviceosapi.dto.request.VehicleAddRequest;
import com.example.deviceosapi.dto.request.VehicleBindRequest;
import com.example.deviceosapi.dto.response.VehicleVerificationRequest;
import com.example.deviceosapi.model.Vehicle;
import com.example.deviceosapi.service.VehicleService;
import com.example.deviceosapi.utils.SecurityUtils;
import com.example.deviceosapi.vo.request.VehicleSearchRequest;
import com.example.deviceosapi.vo.response.VehicleDetailVO;
import com.example.deviceosapi.vo.response.VehicleSearchVO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class VehicleController {

    private final VehicleService vehicleService;


    /**
     * 新增车辆信息
     * */
    @PostMapping("/vehicle")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<Vehicle> addVehicleInfo(@Valid @RequestBody VehicleAddRequest request) {
        return ApiResponse.success(vehicleService.addVehicleInfo(request));
    }

    /**
     * 查询车辆信息接口
     * */
    @GetMapping("/vehicle/search")
    public ApiResponse<List<VehicleSearchVO>> searchVehicleInfo(
            @Valid VehicleSearchRequest request
    ) {
        return ApiResponse.success(vehicleService.searchVehicles(request.getPlateNumber()));
    }

    /**
     * 车辆绑定接口：把车牌号对应的车辆绑定到当前登录用户
     * */
    @PostMapping("/vehicle/bind")
    public ApiResponse<Void> bindVehicle(@Valid @RequestBody VehicleBindRequest request) {
        vehicleService.bindVehicle(
                request.getPlateNumber(),
                SecurityUtils.getCurrentUserId()
        );
        return ApiResponse.success("绑定成功", null);
    }

    /**
     * 提交车辆归属核验（简化版：提交即通过）
     * */
    @PostMapping("/vehicle/verification")
    public ApiResponse<Void> submitVerification(@Valid @RequestBody VehicleVerificationRequest request) {
        vehicleService.submitVehicleVerification(
                request.getPlateNumber(),
                SecurityUtils.getCurrentUserId()
        );
        return ApiResponse.success("核验通过", null);
    }

    /**
     * 查询当前用户已绑定的车辆
     * */
    @GetMapping("/vehicle/my")
    public ApiResponse<List<VehicleSearchVO>> myVehicles() {
        return ApiResponse.success(
                vehicleService.myVehicles(SecurityUtils.getCurrentUserId())
        );
    }
    /**
     * 车辆详情（首页展示）
     * */
    @GetMapping("/vehicle/getVehicleDetailId")
    public ApiResponse<VehicleDetailVO> getVehicleDetail(@RequestParam Long id) {
        return ApiResponse.success(
                vehicleService.getVehicleDetail(id, SecurityUtils.getCurrentUserId())
        );
    }


}
