package com.example.deviceosapi.service.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.deviceosapi.common.BusinessException;
import com.example.deviceosapi.common.ResultCode;
import com.example.deviceosapi.dto.request.VehicleAddRequest;
import com.example.deviceosapi.mapper.VehicleBindMapper;
import com.example.deviceosapi.mapper.VehicleMapper;
import com.example.deviceosapi.mapper.VehicleTelemetryMapper;
import com.example.deviceosapi.model.Vehicle;
import com.example.deviceosapi.service.VehicleService;
import com.example.deviceosapi.vo.response.VehicleDetailVO;
import com.example.deviceosapi.vo.response.VehicleSearchVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VehicleServiceImpl implements VehicleService {

    private final VehicleMapper vehicleMapper;
    private final VehicleBindMapper vehicleBindMapper;
    private final VehicleTelemetryMapper vehicleTelemetryMapper;

    /**车辆新增功能（可带初始里程/电量等状态）*/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Vehicle addVehicleInfo(VehicleAddRequest request) {
        String vin = request.getVin() == null ? "" : request.getVin().trim();
        String brand = request.getBrand() == null ? "" : request.getBrand().trim();
        String model = request.getModel() == null ? "" : request.getModel().trim();
        String plateNumber = request.getPlateNumber() == null ? "" : request.getPlateNumber().trim();

        // 1. 校验必填参数
        if (vin.isEmpty()) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "VIN不能为空");
        }
        if (brand.isEmpty()) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "车辆品牌不能为空");
        }
        if (model.isEmpty()) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "车辆型号不能为空");
        }
        if (plateNumber.isEmpty()) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "车牌号不能为空");
        }

        //查询车辆是否存在
        LambdaQueryWrapper<Vehicle> vinWrapper = new LambdaQueryWrapper<>();
        vinWrapper.eq(Vehicle::getVin, vin);
        Vehicle existVin = vehicleMapper.selectOne(vinWrapper);
        if (existVin != null) {
            throw new BusinessException(ResultCode.CONFLICT, "车辆已存在");
        }
        //判断车牌是否存在
        if (!plateNumber.isEmpty()) {
            LambdaQueryWrapper<Vehicle> plateWrapper = new LambdaQueryWrapper<>();
            plateWrapper.eq(Vehicle::getPlateNumber, plateNumber);
            Vehicle existPlate = vehicleMapper.selectOne(plateWrapper);
            if (existPlate != null) {
                throw new BusinessException(ResultCode.CONFLICT, "车牌已存在");
            }
        }
        //创建车辆对象
        Vehicle vehicle = new Vehicle();
        vehicle.setVin(vin);
        vehicle.setBrand(brand);
        vehicle.setModel(model);
        vehicle.setPlateNumber(plateNumber);
        //保存车辆信息
        vehicleMapper.insert(vehicle);

        // 填了初始状态才写遥测记录，没填的车详情页显示 "--"
        if (hasTelemetry(request)) {
            BigDecimal ratedRange = defaultIfNull(request.getRatedRangeKm(), new BigDecimal("400.00"));
            BigDecimal mileage = defaultIfNull(request.getMileageKm(), BigDecimal.ZERO);
            BigDecimal battery = defaultIfNull(request.getBatteryLevel(), new BigDecimal("100.00"));
            BigDecimal remaining = defaultIfNull(request.getRemainingRangeKm(), ratedRange);
            BigDecimal health = defaultIfNull(request.getBatteryHealth(), new BigDecimal("100.00"));
            String driving = request.getDrivingStatus() == null ? "PARKED" : request.getDrivingStatus();

            vehicleTelemetryMapper.insertTelemetry(
                    vehicle.getId(), mileage, battery, remaining, health, ratedRange, driving);
            vehicleTelemetryMapper.insertTelemetryHistory(
                    vehicle.getId(), mileage, battery, remaining, health, driving);
        }
        return vehicle;
    }

    /**是否填了初始状态字段*/
    private boolean hasTelemetry(VehicleAddRequest request) {
        return request.getMileageKm() != null
                || request.getBatteryLevel() != null
                || request.getRemainingRangeKm() != null
                || request.getBatteryHealth() != null
                || request.getRatedRangeKm() != null
                || request.getDrivingStatus() != null;
    }

    private BigDecimal defaultIfNull(BigDecimal value, BigDecimal defaultValue) {
        return value == null ? defaultValue : value;
    }

    /**
     * 绑定车辆：把车牌号对应的车辆绑定到当前登录用户
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void bindVehicle(String plateNumber, Long userId) {
        String plate = plateNumber == null ? "" : plateNumber.trim();
        if (plate.isEmpty()) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "请输入车牌号");
        }
        //根据车牌号查询车辆并加锁
        VehicleBindMapper.VehicleRow vehicleRow = vehicleBindMapper.selectVehicleForUpdate(plate);
        if (vehicleRow == null) {
            throw new BusinessException(ResultCode.NOT_FOUND, "车辆不存在");
        }
        //检测车辆状态
        if (!"ACTIVE".equals(vehicleRow.status)) {
            throw new BusinessException(ResultCode.CONFLICT, "车辆状态异常");
        }
        //检查车辆是否已绑定其他用户
        if (vehicleRow.ownerUserId != null && !vehicleRow.ownerUserId.equals(userId)) {
            throw new BusinessException(ResultCode.CONFLICT, "车辆已绑定其他用户");
        }
        // 检查车辆归属核验是否通过
        if (vehicleBindMapper.countVerified(userId, vehicleRow.id) == 0) {
            throw new BusinessException(ResultCode.FORBIDDEN, "请先完成车辆归属核验");
        }
        // 检查是否重复绑定
        if (vehicleBindMapper.countActiveBinding(userId, vehicleRow.id) > 0) {
            throw new BusinessException(ResultCode.CONFLICT, "该车辆已经绑定");
        }
        //更新车辆当前所有人
        vehicleBindMapper.updateOwner(vehicleRow.id, userId);
        //插入用户车辆绑定记录
        vehicleBindMapper.insertBinding(userId, vehicleRow.id);
    }

    /**
     * 提交车辆归属核验
     * <p>
     * 当前为简化实现：提交后直接置为 VERIFIED（verification_method = DEMO）。
     * 真实场景应由车厂数据或人工审核后再更新核验结果。
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void submitVehicleVerification(String plateNumber, Long userId) {
        String plate = plateNumber == null ? "" : plateNumber.trim();
        if (plate.isEmpty()) {
            throw new BusinessException(ResultCode.PARAM_ERROR, "请输入车牌号");
        }
        Long vehicleId = vehicleBindMapper.selectVehicleIdByPlateNumber(plate);
        if (vehicleId == null) {
            throw new BusinessException(ResultCode.NOT_FOUND, "车辆不存在");
        }
        //已经核验过就不重复写记录
        if (vehicleBindMapper.countVerified(userId, vehicleId) > 0) {
            return;
        }
        vehicleBindMapper.insertVerification(userId, vehicleId);
    }

    /**
     * 查询当前用户已绑定的车辆
     */
    @Override
    public List<VehicleSearchVO> myVehicles(Long userId) {
        return vehicleBindMapper.selectMyVehicles(userId);
    }

    /**
     * 查询车辆信息
     * */
    @Override
    public List<VehicleSearchVO> searchVehicles(String plateNumber) {
        String keyword = plateNumber == null ? "" : plateNumber.trim().toUpperCase();
        return vehicleBindMapper.searchVehicles(keyword);
    }

    /**
     *  车辆详情（首页展示）
     * */
    @Override
    public VehicleDetailVO getVehicleDetail(Long userId, Long vehicleId) {
        VehicleDetailVO detailVO = vehicleBindMapper.selectVehicleDetail(userId, vehicleId);
        if(detailVO == null){
            // 车辆不存在、已停用，或当前用户没绑定这辆车，统一按"不存在"返回，不暴露车辆是否存在
            throw new BusinessException(ResultCode.NOT_FOUND, "车辆不存在或未绑定");
        }
        detailVO.setVin(desensitizeVin(detailVO.getVin()));
        return detailVO;
    }

    /**
     *  VIN 脱敏：前3位 + 中间打码 + 后4位
     * */
    private String desensitizeVin(String vin) {
        if (vin == null || vin.length() <= 7) {
            return vin;
        }
        return vin.substring(0, 3)
                + "*".repeat(vin.length() - 7)
                + vin.substring(vin.length() - 4);
    }

}
