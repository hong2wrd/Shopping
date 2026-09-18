package com.shopping.member.controller;

import com.shopping.member.dto.request.RoleChangeReqDto;
import com.shopping.member.service.AdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;

    /**
     * 전체 회원 조회
     * @param pageable 페이징 정보
     * @return 전체 회원 정보
     */
    @GetMapping("/members")
    public ResponseEntity<?> searchAllMember(@PageableDefault(
                                                page = 1,
                                                size = 20,
                                                sort = "createdAt",
                                                direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity
                .ok()
                .body(adminService.searchAllMember(pageable));
    }

    /**
     * 권한을 변경
     * @param roleChangeReqDto 권한을 변경할 사용자의 DTO
     */
    @PatchMapping("/change-role")
    public ResponseEntity<Void> changeRole(@RequestBody @Valid RoleChangeReqDto roleChangeReqDto) {
        adminService.changeRole(roleChangeReqDto);
        return ResponseEntity.ok().build();
    }
}
