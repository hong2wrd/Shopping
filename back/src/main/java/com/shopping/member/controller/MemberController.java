package com.shopping.member.controller;

import com.shopping.common.dto.ResponseDto;
import com.shopping.member.dto.request.MemberJoinReqDto;
import com.shopping.member.dto.request.MemberPasswordUpdateReqDto;
import com.shopping.member.dto.request.MemberUpdateReqDto;
import com.shopping.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    /**
     * 사용자 가입
     * @param memberJoinReqDto 가입정보
     * @return
     */
    @PostMapping
    public ResponseEntity<?> join(@RequestBody @Valid MemberJoinReqDto memberJoinReqDto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseDto<>(1, "가입이 완료되었습니다.", memberService.join(memberJoinReqDto)));
    }

    /**
     * 사용자 조회
     * @param userDetails 사용자 정보
     * @return 사용자 조회 DTO
     */
    @GetMapping
    public ResponseEntity<?> findMember(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDto<>(1, "조회가 완료되었습니다.", memberService.findMember(userDetails.getUsername())));
    }

    /**
     * 사용자 정보 수정
     * @param memberUpdateReqDto 사용자 정보 수정 DTO
     * @param userDetails 사용자 정보
     * @return 사용자 이름 DTO
     */
    @PutMapping
    public ResponseEntity<?> updateMember(@RequestBody @Valid MemberUpdateReqDto memberUpdateReqDto,
                                          @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDto<>(1, "수정이 완료되었습니다.", memberService.updateMember(memberUpdateReqDto, userDetails.getUsername())));
    }

    /**
     * 사용자 비밀번호 변경
     * @param memberPasswordUpdateReqDto 사용자 비밀번호 변경 DTO
     * @param userDetails 사용자 정보
     * @return 사용자 이름 DTO
     */
    @PatchMapping("/password")
    public ResponseEntity<?> updatePassword(@RequestBody @Valid MemberPasswordUpdateReqDto memberPasswordUpdateReqDto,
                                             @AuthenticationPrincipal UserDetails userDetails) {
        memberService.updatePassword(memberPasswordUpdateReqDto, userDetails.getUsername());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDto<>(1, "수정이 완료되었습니다."));
    }

    @PatchMapping("/retire")
    public ResponseEntity<?> retire(@AuthenticationPrincipal UserDetails userDetails) {
        memberService.retire(userDetails.getUsername());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDto<>(1, "탈퇴가 완료되었습니다."));
    }
}
