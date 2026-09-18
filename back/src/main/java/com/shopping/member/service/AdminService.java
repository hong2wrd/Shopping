package com.shopping.member.service;

import com.shopping.common.exception.BusinessException;
import com.shopping.common.exception.ErrorCode;
import com.shopping.member.dto.request.RoleChangeReqDto;
import com.shopping.member.dto.response.MemberRespDto;
import com.shopping.member.entity.Member;
import com.shopping.member.entity.Role;
import com.shopping.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class AdminService {

    private final MemberRepository memberRepository;

    /**
     * 회원 권한 변경
     * @param reqDto 변경할 회원 정보
     */
    public void changeRole(RoleChangeReqDto reqDto) {
        Member targetMember = memberRepository.findByMemIdAndRetiredIsFalse(reqDto.getMemId())
                .orElseThrow(() -> new BusinessException(ErrorCode.MEM_NOT_FOUND));

        targetMember.changeRole(Role.valueOf(reqDto.getChangeRole()));
    }

    /**
     * 전체 회원 조회
     * @param pageable 페이징
     */
    public List<MemberRespDto> searchAllMember(Pageable pageable) {
        return memberRepository.findAllMemberDto(pageable).toList();
    }
}
