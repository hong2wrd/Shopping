package com.shopping.common.controller;

import com.shopping.common.exception.BusinessException;
import com.shopping.common.exception.ErrorCode;
import com.shopping.common.jwt.JwtProperties;
import com.shopping.common.jwt.JwtType;
import com.shopping.common.redis.repository.RedisRefreshTokenRepository;
import com.shopping.common.security.MemberDetails;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.shopping.common.jwt.JwtUtil.*;
import static com.shopping.common.util.ResponseUtil.removeCookie;

@RestController
@RequiredArgsConstructor
@RequestMapping("/common")
public class CommonController {

    private final RedisRefreshTokenRepository redisRefreshTokenRepository;
    private final JwtProperties jwtProperties;

    @Value("${cookie.secure}")
    private boolean secure;

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@CookieValue(name = "Refresh-Token", required = false) String refreshToken,
                                       @AuthenticationPrincipal UserDetails userDetails,
                                       HttpServletResponse response) {

        if(refreshToken == null) {
            throw new BusinessException(ErrorCode.LOGOUT_FAIL);
        }

        String memIdByToken = getMemberId(refreshToken, jwtProperties)
                .orElseThrow(() -> new BusinessException(ErrorCode.LOGOUT_FAIL));

        if(!memIdByToken.equals(userDetails.getUsername())) {
            throw new BusinessException(ErrorCode.LOGOUT_FAIL);
        }

        redisRefreshTokenRepository.delete(memIdByToken);

        removeCookie(response, jwtProperties, secure);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@CookieValue("Refresh-Token") String refreshToken) {
        String memIdByToken = getMemberId(refreshToken, jwtProperties)
                .orElseThrow(() -> new BusinessException(ErrorCode.REFRESH_TOKEN_VALIDATION_FAILED));

        String findRefreshToken = redisRefreshTokenRepository.findRefreshTokenByMemberId(memIdByToken)
                .orElseThrow(() -> new BusinessException(ErrorCode.REFRESH_TOKEN_EXPIRED));

        if(!findRefreshToken.equals(refreshToken)) {
            throw new BusinessException(ErrorCode.REFRESH_TOKEN_VALIDATION_FAILED);
        }

        MemberDetails memberDetails = (MemberDetails) verify(refreshToken, jwtProperties);

        String newAccessToken = create(memberDetails.member(), jwtProperties, JwtType.ACCESS);

        return ResponseEntity.ok(newAccessToken);
    }
}
