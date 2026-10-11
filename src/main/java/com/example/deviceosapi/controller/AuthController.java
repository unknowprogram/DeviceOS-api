package com.example.deviceosapi.controller;

import com.example.deviceosapi.common.ApiResponse;
import com.example.deviceosapi.common.BusinessException;
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

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<Void> register(@Valid @RequestBody RegisterRequest request) {
        authService.register(
                request.getPhone(),
                request.getPassword()
        );
        return ApiResponse.success("注册成功", null);
    }

    /**
     * 注册请求参数
     * */
    @Data
    public static class RegisterRequest{
        @NotBlank(message = "手机号不能为空")
        @Pattern(
                regexp = "^1[3-9]\\d{9}$",
                message = "手机号格式不正确"
        )
        private String phone;
        @NotBlank(message = "密码不能为空")
        @Size(
                min = 6,
                max = 20,
                message = "密码长度在6到20个字符之间"
        )
        private String password;
    }

}
