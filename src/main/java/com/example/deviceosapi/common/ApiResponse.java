package com.example.deviceosapi.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 接口统一返回格式
 * */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
    private Integer code;
    private String message;
    private T data;

    //成功
    public static <T> ApiResponse<T> success(T data){
        return new ApiResponse<>(200, "success", data);
    }
    // 成功：自定义提示
    public static <T> ApiResponse<T> success(
            String message, T data) {
        return new ApiResponse<>(200, message, data);
    }

    // 失败：没有数据
    public static <T> ApiResponse<T> error(
            Integer code, String message) {
        return new ApiResponse<>(code, message, null);
    }

}
