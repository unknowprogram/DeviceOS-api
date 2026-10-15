package com.example.deviceosapi.controller;

import com.example.deviceosapi.common.ApiResponse;
import com.example.deviceosapi.dto.VehicleAddRequest;
import com.example.deviceosapi.model.Vehicle;
import com.example.deviceosapi.service.VehicleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class VehicleController {
    private final VehicleService vehicleService;

    @PostMapping("/vehicle")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<Vehicle> addVehicleInfo(
            @Valid @RequestBody VehicleAddRequest request
            ) {

        Vehicle vehicle = vehicleService.addVehicleInfo(
                request.getVin(),
                request.getBrand(),
                request.getModel(),
                request.getPlateNumber()
        );

        return ApiResponse.success(vehicle);

    }

}
