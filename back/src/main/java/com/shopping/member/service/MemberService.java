package com.shopping.member.service;

import com.shopping.common.exception.BusinessException;
import com.shopping.common.exception.ErrorCode;
import com.shopping.member.dto.request.MemberJoinReqDto;
import com.shopping.member.dto.response.MemberJoinRespDto;
import com.shopping.member.entity.Member;
import com.shopping.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 사용자 가입
     * @param memberJoinReqDto 가입 정보
     * @return 가입한 사용자의 Id, Name DTO
     */
    public MemberJoinRespDto join( MemberJoinReqDto memberJoinReqDto) {
        memberRepository.findByMemIdAndRetiredIsFalse(memberJoinReqDto.getMemId())
                .ifPresent(member -> {
                    throw new BusinessException(ErrorCode.MEM_CONFIRM);
                });

        String encodedPassword = passwordEncoder.encode(memberJoinReqDto.getPassword());

        Member member = Member.create(memberJoinReqDto.getMemId(), memberJoinReqDto.getName(), encodedPassword);

        memberRepository.save(member);

        return new MemberJoinRespDto(member.getMemId(), member.getName());
    }
}
