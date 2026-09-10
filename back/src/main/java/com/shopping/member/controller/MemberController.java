package com.shopping.member.controller;

import com.shopping.common.dto.ResponseDto;
import com.shopping.member.dto.request.MemberJoinReqDto;
import com.shopping.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    /**
     * 사용자 가압
     * @param memberJoinReqDto 가입정보
     * @return
     */
    @PostMapping
    public ResponseEntity<?> join(@RequestBody @Valid MemberJoinReqDto memberJoinReqDto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseDto<>(1, "가입이 완료되었습니다.", memberService.join(memberJoinReqDto)));
    }
}
