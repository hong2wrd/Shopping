package com.shopping.common.security;

import com.shopping.member.entity.Member;
import com.shopping.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MemberDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String memId) throws UsernameNotFoundException {
        Member findMember = memberRepository.findByMemIdAndRetiredIsFalse(memId)
                .orElseThrow(() -> new InternalAuthenticationServiceException("아이디 및 비밀번호를 확인해주세요."));
        return new MemberDetails(findMember);
    }
}
