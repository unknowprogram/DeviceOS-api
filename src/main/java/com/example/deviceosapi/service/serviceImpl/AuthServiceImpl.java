package com.example.deviceosapi.service.serviceImpl;

import com.example.deviceosapi.model.User;
import com.example.deviceosapi.repository.UserRepository;
import com.example.deviceosapi.service.AuthService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void register(String phone, String password) {
        //验证手机号是否存在
        if(userRepository.existsByPhone(phone)){
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "该手机号已存在"
            );
        }
        //创建用户
        User user = new User();
        user.setPhone(phone);
        user.setPasswordHash(passwordEncoder.encode(password));
        //保存用户
        userRepository.save(user);
    }
}
