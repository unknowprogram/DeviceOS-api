package com.example.deviceosapi.controller;

import com.example.deviceosapi.common.ApiResponse;
import com.example.deviceosapi.dto.request.LoginRequest;
import com.example.deviceosapi.dto.response.LoginResponse;
import com.example.deviceosapi.dto.request.RegisterRequest;
import com.example.deviceosapi.dto.request.SendCodeRequest;
import com.example.deviceosapi.dto.response.SendCodeResponse;
import com.example.deviceosapi.dto.response.UserInfoResponse;
import com.example.deviceosapi.service.AuthService;
import com.example.deviceosapi.utils.SecurityUtils;
import jakarta.validation.Valid;
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

    /**
     * 获取用户信息
     * */
    @GetMapping("/getUserInfo")
    public ApiResponse<UserInfoResponse> getUserInfo() {
        return ApiResponse.success(
                authService.getUserInfo(SecurityUtils.getCurrentUserId())
        );
    }

}
