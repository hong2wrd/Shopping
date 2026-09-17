package com.shopping.common.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.shopping.common.security.MemberDetails;
import com.shopping.member.entity.Member;
import com.shopping.member.entity.Role;
import org.springframework.security.authentication.InternalAuthenticationServiceException;

import java.util.Date;
import java.util.Optional;


public class JwtUtil {
    /**
     * 토큰 생성
     * return AccessToken or RefreshToken
     */
    public static String create(Member member, JwtProperties jwtProperties, JwtType jwtType) {
        String prefix = JwtType.ACCESS.equals(jwtType) ? jwtProperties.getPrefix() : "";
        Long expirationTime = JwtType.ACCESS.equals(jwtType) ? jwtProperties.getAccessExpirationTime() : jwtProperties.getRefreshExpirationTime();

        String jwtToken = JWT.create()
                .withSubject("MY_TODO")
                .withExpiresAt(new Date(System.currentTimeMillis() + expirationTime))
                .withClaim("id", member.getMemId())
                .withClaim("name", member.getName())
                .withClaim("role", member.getRole().name())
                .withClaim("type", jwtType.toString())
                .sign(Algorithm.HMAC512(jwtProperties.getSecret()));
        return prefix + jwtToken;
    }

    /**
     * 토큰 확인
     * return MemberDetails
     */
    public static org.springframework.security.core.userdetails.UserDetails verify(String token, JwtProperties jwtProperties) {
        try {
            DecodedJWT decodedJwt = JWT
                    .require(Algorithm.HMAC512(jwtProperties.getSecret()))
                    .build()
                    .verify(token.replace(jwtProperties.getPrefix(), ""));
            String id = decodedJwt.getClaim("id").asString();
            String role = decodedJwt.getClaim("role").asString();
            String name = decodedJwt.getClaim("name").asString();
            return new MemberDetails(id, "", name, Role.valueOf(role));
        } catch(JWTVerificationException e) {
            throw new InternalAuthenticationServiceException( "JWT가 만료되었거나 유효하지 않습니다.", e);
        }
    }

    /**
     * 토큰에서 회원 아이디 추출
     * @param token 토큰
     * @param jwtProperties JWT 프로퍼티
     * @return 회원 아이디
     */
    public static Optional<String> getMemberId(String token, JwtProperties jwtProperties) {
        return Optional.ofNullable(
                JWT.decode(token.replace(jwtProperties.getPrefix(), "")).getClaim("id").asString()
        );
    }


}
