package com.example.deviceosapi.service.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.deviceosapi.dto.VehicleAddRequest;
import com.example.deviceosapi.mapper.VehicleMapper;
import com.example.deviceosapi.model.Vehicle;
import com.example.deviceosapi.service.VehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VehicleServiceImpl implements VehicleService {

    private final VehicleMapper vehicleMapper;
    /**车辆新增功能*/
    @Override
    public Vehicle addVehicleInfo(String vin, String brand, String model, String plateNumber) {
        // 1. 校验必填参数
        if (vin == null || vin.trim().isEmpty()) {
            throw new RuntimeException("VIN不能为空");
        }
        if (brand == null || brand.trim().isEmpty()) {
            throw new RuntimeException("车辆品牌不能为空");
        }
        if (model == null || model.trim().isEmpty()) {
            throw new RuntimeException("车辆型号不能为空");
        }
        if (plateNumber == null || plateNumber.trim().isEmpty()) {
            throw new RuntimeException("车牌号不能为空");
        }
        // 2. 去除参数前后的空格
        vin = vin.trim();
        brand = brand.trim();
        model = model.trim();
        plateNumber = plateNumber.trim();

        //查询车辆是否存在
        LambdaQueryWrapper<Vehicle> vinWrapper  = new LambdaQueryWrapper<>();
        vinWrapper .eq(Vehicle::getVin, vin);
        Vehicle existVin  = vehicleMapper.selectOne(vinWrapper );
        if (existVin  != null) {
            throw new RuntimeException("车辆已存在");
        }
        //判断车牌是否存在
        if (!plateNumber.isEmpty() && !plateNumber.trim().isEmpty()){
            LambdaQueryWrapper<Vehicle> plateWrapper  = new LambdaQueryWrapper<>();
            plateWrapper .eq(Vehicle::getPlateNumber, plateNumber);
            Vehicle existPlate  = vehicleMapper.selectOne(plateWrapper);
            if (existPlate != null) {
                throw new RuntimeException("车牌已存在");
            }
        }
        //创建车辆对象
        Vehicle vehicle = new Vehicle();
        vehicle.setVin(vin);
        vehicle.setBrand(brand);
        vehicle.setModel(model);
        vehicle.setPlateNumber(
                plateNumber
        );
        //保存车辆信息
        vehicleMapper.insert(vehicle);
        return vehicle;
    }
}
