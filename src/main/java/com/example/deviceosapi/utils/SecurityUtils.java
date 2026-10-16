package com.example.deviceosapi.utils;

import com.example.deviceosapi.common.BusinessException;
import com.example.deviceosapi.common.ResultCode;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * 当前登录用户工具类
 * <p>
 * JwtAuthenticationFilter 会把用户 id 作为 principal 放进 SecurityContext，
 * 这里统一取出来，Service / Controller 都可以用。
 */
public final class SecurityUtils {

    private SecurityUtils() {
    }

    /**
     * 取当前登录用户 id，未登录时抛 401
     */
    public static Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof Long userId)) {
            throw new BusinessException(ResultCode.UNAUTHORIZED, "请先登录");
        }
        return userId;
    }
}
