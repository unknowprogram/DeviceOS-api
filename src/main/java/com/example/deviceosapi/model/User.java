package com.example.deviceosapi.model;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 用户表 dos_user
 */
@Data
@NoArgsConstructor
@TableName("dos_user")
public class User {

    /** 主键，数据库自增 */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 手机号 */
    private String phone;

    /** 密码哈希 */
    @TableField("password_hash")
    private String passwordHash;

    /** 昵称 */
    private String nickname;

    /** 状态：1 正常，0 禁用 */
    private Integer status = 1;

    /** 创建时间，插入时由 MetaObjectHandler 自动填充 */
    @TableField(value = "created_at", fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    /** 更新时间，插入和更新时由 MetaObjectHandler 自动填充 */
    @TableField(value = "updated_at", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}
