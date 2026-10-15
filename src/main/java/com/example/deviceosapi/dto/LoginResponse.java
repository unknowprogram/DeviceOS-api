package com.example.deviceosapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 登录响应
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse{
    /** JWT，后续请求放在 Authorization: Bearer <token> */
    private String token;
    /** 有效期，单位毫秒 */
    private Long expiresIn;
    /** 昵称*/
    private String nickName;
}
