package com.example.deviceosapi.controller;

import com.example.deviceosapi.common.ApiResponse;
import com.example.deviceosapi.common.BusinessException;
import com.example.deviceosapi.dto.LoginRequest;
import com.example.deviceosapi.dto.LoginResponse;
import com.example.deviceosapi.dto.RegisterRequest;
import com.example.deviceosapi.dto.SendCodeRequest;
import com.example.deviceosapi.dto.SendCodeResponse;
import com.example.deviceosapi.service.AuthService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;


    /**
     * 注册接口
     * */
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<Void> register(@Valid @RequestBody RegisterRequest request) {
        authService.register(
                request.getPhone(),
                request.getCode(),
                request.getPassword()
        );
        return ApiResponse.success("注册成功", null);
    }

    /**
     * 发送验证码
     * */
    @PostMapping("/sendCode")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<SendCodeResponse> sendCode(
            @Valid @RequestBody SendCodeRequest request
            ){
        String code = authService.generateCode(
                request.getPhone()
        );

        return ApiResponse.success("验证码已发送", new SendCodeResponse(code));
    }

    /**
     * 登录接口
     * */
    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(
            @Valid @RequestBody LoginRequest request
    ){
        LoginResponse loginResponse = authService.login(
                request.getPhone(),
                request.getPassword()
        );
        return ApiResponse.success("登录成功", loginResponse);

    }


}
