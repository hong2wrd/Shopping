package com.shopping.member.service;

import com.shopping.common.exception.BusinessException;
import com.shopping.common.exception.ErrorCode;
import com.shopping.member.dto.request.MemberJoinReqDto;
import com.shopping.member.dto.request.MemberPasswordUpdateReqDto;
import com.shopping.member.dto.request.MemberUpdateReqDto;
import com.shopping.member.dto.response.MemberJoinRespDto;
import com.shopping.member.dto.response.MemberRespDto;
import com.shopping.member.dto.response.MemberUpdateRespDto;
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

    /**
     * 사용자 조회
     * @param memId 사용자 Id
     * @return 사용자 정보 DTO
     */
    @Transactional(readOnly = true)
    public MemberRespDto findMember(String memId) {
        Member findMember = memberRepository.findByMemIdAndRetiredIsFalse(memId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEM_NOT_FOUND));
        return new MemberRespDto(findMember.getMemId(), findMember.getName(), findMember.getMemberShip().name(), findMember.getRole().name(), findMember.isRetired(), "");
    }

    /**
     * 사용자 정보 수정
     * @param memberUpdateReqDto 사용자 정보 수정 DTO
     * @param memId 사용자 Id
     * @return 사용자 이름 DTO
     */
    public MemberUpdateRespDto updateMember(MemberUpdateReqDto memberUpdateReqDto, String memId) {
        Member findMember = memberRepository.findByMemIdAndRetiredIsFalse(memId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEM_NOT_FOUND));
        findMember.updateName(memberUpdateReqDto.getName());
        return new MemberUpdateRespDto(findMember.getName());
    }

    /**
     * 사용자 비밀번호 변경
     * @param memberPasswordUpdateReqDto 사용자 비밀번호 변경 DTO
     * @param memId 사용자 Id
     */
    public void updatePassword(MemberPasswordUpdateReqDto memberPasswordUpdateReqDto, String memId) {
        Member findMember = memberRepository.findByMemIdAndRetiredIsFalse(memId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEM_NOT_FOUND));
        findMember.updatePassword(passwordEncoder.encode(memberPasswordUpdateReqDto.getPassword()));
    }

    /**
     * 사용자 탈퇴
     * @param memId 사용자 Id
     */
    public void retire(String memId) {
        Member findMember = memberRepository.findByMemIdAndRetiredIsFalse(memId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEM_NOT_FOUND));
        findMember.retire();
    }
}
