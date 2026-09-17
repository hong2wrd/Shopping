package com.shopping.common.security;

import com.shopping.member.entity.Member;
import com.shopping.member.entity.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

/**
 * User Details 객체를 생성하기 위한 클래스입니다.
 * @param memId 회원 아이디
 * @param password 비밀번호
 * @param role 권한
 */
public record MemberDetails(String memId, String name, String password, Role role) implements UserDetails {

    public MemberDetails {
        if (memId == null || memId.isBlank()) {
            throw new IllegalArgumentException("memId is null or blank");
        }

        if (role == null) {
            throw new IllegalArgumentException("role is null");
        }

    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(() -> "ROLE_" + role.name());
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return memId;
    }

    /**
     * Member 객체를 반환합니다.
     * @return Member 객체
     */
    public Member member() {
        return Member.of(memId, name, role.toString());
    }
}
