package com.hrms.utils;

import com.hrms.common.BusinessException;
import com.hrms.common.CurrentUser;
import com.hrms.dto.response.AuthUser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Optional;

/**
 * Token 工具，负责登录 Token 的生成和解析。
 */
@Component
public class TokenService {
    @Value("${hrms.auth.secret:hrms-demo-secret}")
    private String secret = "hrms-demo-secret";

    @Value("${hrms.auth.token-valid-hours:8}")
    private long validHours = 8;

    public String generate(AuthUser user) {
        long expiresAt = Instant.now().plus(validHours, ChronoUnit.HOURS).toEpochMilli();
        String payload = user.username + "|" + user.name + "|" + user.role + "|" + expiresAt;
        String encodedPayload = base64Url(payload.getBytes(StandardCharsets.UTF_8));
        return encodedPayload + "." + sign(encodedPayload);
    }

    public Optional<CurrentUser> parse(String token) {
        if (token == null || !token.contains(".")) {
            return Optional.empty();
        }
        String[] parts = token.split("\\.", 2);
        if (!sign(parts[0]).equals(parts[1])) {
            return Optional.empty();
        }
        String payload = new String(Base64.getUrlDecoder().decode(parts[0]), StandardCharsets.UTF_8);
        String[] values = payload.split("\\|", 4);
        if (values.length != 4 || Long.parseLong(values[3]) < Instant.now().toEpochMilli()) {
            return Optional.empty();
        }
        return Optional.of(new CurrentUser(values[0], values[1], values[2]));
    }

    public long getValidHours() {
        return validHours;
    }

    private String sign(String value) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            return base64Url(mac.doFinal(value.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception ex) {
            throw new BusinessException(500, "Token 签名失败");
        }
    }

    private String base64Url(byte[] bytes) {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }
}
