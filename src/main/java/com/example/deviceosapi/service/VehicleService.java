package com.example.deviceosapi.service;

import com.example.deviceosapi.dto.VehicleAddRequest;
import com.example.deviceosapi.model.Vehicle;

public interface VehicleService {
    /**新增车辆信息*/
    Vehicle addVehicleInfo(String vin, String brand, String model, String plateNumber);
}
