package com.hrms.common;

import com.hrms.utils.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;
import java.util.Arrays;

/**
 * 登录鉴权拦截器，校验 Bearer Token 并执行接口角色权限检查。
 */
@Component
public class AuthInterceptor implements HandlerInterceptor {
    public static final String CURRENT_USER_ATTRIBUTE = "currentUser";
    private final TokenService tokenService;

    public AuthInterceptor(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }
        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            writeError(response, 401, "缺少登录凭证");
            return false;
        }
        var user = tokenService.parse(header.substring(7));
        if (user.isEmpty()) {
            writeError(response, 401, "登录凭证无效或已过期");
            return false;
        }
        request.setAttribute(CURRENT_USER_ATTRIBUTE, user.get());
        if (handler instanceof HandlerMethod handlerMethod) {
            RoleRequired roleRequired = handlerMethod.getMethodAnnotation(RoleRequired.class);
            if (roleRequired == null) {
                roleRequired = handlerMethod.getBeanType().getAnnotation(RoleRequired.class);
            }
            if (roleRequired != null && Arrays.stream(roleRequired.value()).noneMatch(user.get().role()::equals)) {
                writeError(response, 403, "当前账号无权限访问该接口");
                return false;
            }
        }
        return true;
    }

    private void writeError(HttpServletResponse response, int code, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_OK);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write("{\"code\":" + code + ",\"message\":\"" + message + "\",\"data\":null}");
    }
}
