package com.shopping.common.util;

import com.shopping.common.dto.ResponseDto;
import com.shopping.common.jwt.JwtProperties;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import tools.jackson.databind.ObjectMapper;

import java.time.Duration;

@Slf4j
public class ResponseUtil {

    /**
     * 응답 DTO를 JSON으로 직렬화하여 HTTP 응답 본문에 작성한다.
     *
     * @param response HTTP 응답 객체
     * @param responseDto JSON으로 변환할 응답 데이터
     * @param httpStatus 응답에 설정할 HTTP 상태 코드
     */
    public static void response(HttpServletResponse response, ResponseDto<?> responseDto, HttpStatus httpStatus) {
        try {
            ObjectMapper om = new ObjectMapper();
            String responseBody = om.writeValueAsString(responseDto);
            response.setContentType("application/json; charset=UTF-8");
            response.setStatus(httpStatus.value());
            response.getWriter().println(responseBody);
        } catch (Exception e) {
            log.error("Server Parsing Error");
        }
    }

    /**
     * 리프레시 토큰 쿠키의 만료 시간을 0으로 설정하여 삭제한다.
     *
     * @param response 쿠키 삭제 헤더를 추가할 HTTP 응답 객체
     * @param jwtProperties 리프레시 토큰 쿠키 이름을 포함한 JWT 설정
     * @param secure 쿠키의 HttpOnly 속성 적용 여부
     */
    public static void removeCookie(HttpServletResponse response, JwtProperties jwtProperties, boolean secure) {
        ResponseCookie cookie = ResponseCookie
                .from(jwtProperties.getRefreshTokenHeader(), "")
                .httpOnly(secure)
                .sameSite("Lax")
                .path("/")
                .maxAge(Duration.ZERO)
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }
}
