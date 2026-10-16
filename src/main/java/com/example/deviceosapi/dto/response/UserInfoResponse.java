package com.example.deviceosapi.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 当前登录用户信息
 * */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoResponse {

    private Long id;

    private String phone;

    private String nickname;
}
