package com.shopping.member.repository;

import com.shopping.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    /**
     * 탈퇴하지 않은 사용자 찾기
     * @param memId 찾을 Member Id
     * @return Member Optional 객체
     */
    Optional<Member> findByMemIdAndRetiredIsFalse(String memId);
}
