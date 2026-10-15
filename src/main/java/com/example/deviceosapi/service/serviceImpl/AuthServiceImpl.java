package com.example.deviceosapi.service.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.deviceosapi.common.BusinessException;
import com.example.deviceosapi.common.ResultCode;
import com.example.deviceosapi.dto.LoginResponse;
import com.example.deviceosapi.mapper.SmsCodeMapper;
import com.example.deviceosapi.mapper.UserMapper;
import com.example.deviceosapi.model.SmsCode;
import com.example.deviceosapi.model.User;
import com.example.deviceosapi.service.AuthService;
import com.example.deviceosapi.utils.JwtUtil;
import com.example.deviceosapi.utils.SmsCodeUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final SmsCodeMapper smsCodeMapper;
    private final JwtUtil jwtUtil;

    @Override
    @Transactional
    public void register(String phone, String code, String password) {
        //查询最新的一条验证码
        SmsCode smsCode = smsCodeMapper.selectOne(
                new LambdaQueryWrapper<SmsCode>()
                        .eq(SmsCode::getPhone, phone)
                        .eq(SmsCode::getPurpose, "register")
                        .orderByDesc(SmsCode::getCreatedAt)
                        .last("LIMIT 1")
        );
        if (smsCode == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "验证码不存在"
            );
        }
        //判断过期
        if (smsCode.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "验证码已过期"
            );
        }
        //校验验证码
        if (!smsCode.getCodeHash().equals(code)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "验证码错误"
            );
        }

        //验证手机号是否存在
        boolean phoneExists = userMapper.exists(
                new LambdaQueryWrapper<User>().eq(User::getPhone, phone)
        );
        if (phoneExists) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "该手机号已存在"
            );
        }
        //创建用户
        User user = new User();
        user.setPhone(phone);
        user.setPasswordHash(passwordEncoder.encode(password));
        //保存用户，created_at / updated_at 由 MetaObjectHandler 自动填充
        userMapper.insert(user);
    }

    /**
     * 生成验证码
     * */
    @Override
    public String generateCode(String phone) {

        String code = SmsCodeUtil.generateCode();
        SmsCode smsCode = new SmsCode();
        smsCode.setPhone(phone);
        smsCode.setCodeHash(code);
        smsCode.setPurpose("register");
        smsCode.setExpiresAt(LocalDateTime.now().plusMinutes(5));

        smsCodeMapper.insert(smsCode);

        return code;
    }

    /**
     * 登录
     * */
    @Override
    public LoginResponse login(String phone, String password) {
        //按手机号查用户
        User user = userMapper.selectOne(
                new LambdaQueryWrapper<User>()
                        .eq(User::getPhone, phone)
        );
        //用户不存在或密码错误：统一提示，避免暴露手机号是否已注册
        if (user == null || !passwordEncoder.matches(password, user.getPasswordHash())) {
            throw new BusinessException(ResultCode.UNAUTHORIZED, "手机号或密码错误");
        }

        //账号被禁用
        if (user.getStatus() != null && user.getStatus() == 0) {
            throw new BusinessException(ResultCode.FORBIDDEN, "账号已被禁用");
        }
        //签发token
        String token = jwtUtil.generateToken(user.getId());
        return new LoginResponse(token, jwtUtil.getAccessExpirationMs(), user.getNickname());
    }
}
