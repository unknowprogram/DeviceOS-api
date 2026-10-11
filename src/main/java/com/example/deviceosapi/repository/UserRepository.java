package com.example.deviceosapi.repository;

/**
 * 访问用户表，查询手机号、判断是否重复、保存用户
 * */
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.deviceosapi.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    //查询手机号
    Optional<User> findByPhone(String phone);
    //判断手机号是否重复
    boolean existsByPhone(String phone);
}