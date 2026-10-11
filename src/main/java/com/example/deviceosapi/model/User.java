package com.example.deviceosapi.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "dos_user",uniqueConstraints = @UniqueConstraint(
        name = "uk_dos_user_phone",
        columnNames = "phone"
))
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // 手机号
    @Column(nullable = false,length = 11)
    private String phone;

    // 密码
    @Column(name = "password_hash", nullable = false, length = 100)
    private String passwordHash;
    // 昵称
    @Column(length = 50)
    private String nickname;
    //状态
    @Column(nullable = false,columnDefinition = "TINYINT")
    private Integer status = 1;
    // 创建时间
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    // 更新时间

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // 创建前执行
    @PrePersist
    public void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        createdAt = now;
        updatedAt = now;
    }

    // 更新前执行
    @PreUpdate
    public void onUpdate() {
        updatedAt = LocalDateTime.now();
    }






}
