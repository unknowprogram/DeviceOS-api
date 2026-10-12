package com.example.deviceosapi.model;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 短信验证码表 dos_sms_code
 */
@Data
@TableName("dos_sms_code")
public class SmsCode {

    /** 主键，数据库自增 */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 手机号 */
    private String phone;

    /** 验证码 */
    @TableField("code_hash")
    private String codeHash;

    /** 用途 */
    private String purpose;

    /** 创建时间，插入时自动填充 */
    @TableField(value = "created_at", fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    /** 过期时间 */
    @TableField("expires_at")
    private LocalDateTime expiresAt;
}
