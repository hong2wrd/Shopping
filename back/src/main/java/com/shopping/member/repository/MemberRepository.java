package com.shopping.member.repository;

import com.shopping.member.dto.response.MemberRespDto;
import com.shopping.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    /**
     * 탈퇴하지 않은 사용자 찾기
     * @param memId 찾을 Member Id
     * @return Member Optional 객체
     */
    Optional<Member> findByMemIdAndRetiredIsFalse(String memId);

    /**
     * 모든 사용자 조회
     * @param pageable 페이징 정보
     * @return MemberRespDto Page 객체
     */
    @Query("""
        SELECT new com.shopping.member.dto.response.MemberRespDto(
             m.memId,
             m.name,
             String(m.memberShip),
             String(m.role),
             m.retired,
             l.reason
            )
          FROM Member m LEFT JOIN Leave l
           ON m = l.member
    """)
    Page<MemberRespDto> findAllMemberDto(Pageable pageable);

}
