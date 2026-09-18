package com.shopping.member.entity;

import com.shopping.common.entity.BaseEntity;
import com.shopping.member.exception.MemberValidationException;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import java.util.UUID;

@Entity
@Getter
@Builder(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicUpdate
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "member_id", nullable = false, length = 20, updatable = false)
    private String memId;

    @Column(nullable = false, length = 60)
    private String password;
    @Column(nullable = false, length = 20)
    private String name;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private MemberShip memberShip = MemberShip.BRONZE;

    @Builder.Default
    @Column(nullable = false)
    private Role role = Role.MEMBER;

    @Builder.Default
    @Column(nullable = false)
    private boolean retired = false;

    public static Member of(String memId, String name, String role) {
        return Member.builder()
                .memId(memId)
                .name(name)
                .role(Role.valueOf(role))
                .build();
    }

    /**
     * 사용자 생성
     * @param memId 사용자 ID
     * @param name 사용자 이름
     * @param encodedPassword 암호화된 비밀번호
     * @return Member
     */
    public static Member create(String memId, String name, String encodedPassword) {
        memberValidate(memId, name, encodedPassword);

        Member member = Member.builder()
                .memId(memId)
                .name(name)
                .password(encodedPassword)
                .build();
        member.setCreatedBy(memId);

        return member;
    }

    private static void memberValidate(String memId, String name, String encodedPassword) {
        if(memId == null || memId.isBlank()) {
            throw new MemberValidationException("사용자 ID는 필수 사항입니다.");
        }

        if (memId.length() > 20) {
            throw new MemberValidationException("사용자 ID는 20자 이하여야합니다.");
        }

        if(name == null || name.isBlank()) {
            throw new MemberValidationException("사용자 이름은 필수 사항입니다.");
        }

        if (encodedPassword == null || encodedPassword.isBlank()) {
            throw new MemberValidationException("비밀번호는 필수 사항입니다.");
        }
    }

    /**
     * Role 변경
     * @param role 변경할 Role
     */
    public void changeRole(Role role) {
        if(role == null) {
            throw new MemberValidationException("ROLE은 필수 사항입니다.");
        }
        this.role = role;
    }

    /**
     * 이름 변경
     * @param changeName 변경할 이름
     */
    public void updateName(String changeName) {
        if(changeName == null || changeName.isBlank()) {
            throw new MemberValidationException("사용자 이름은 필수 사항입니다.");
        }

        if(changeName.length() > 20) {
            throw new MemberValidationException("사용자 이름은 20자 이하여야합니다.");
        }

        if(changeName.length() < 2) {
            throw new MemberValidationException("사용자 이름은 2자 이상이어야합니다.");
        }

        if(this.name.equals(changeName)) {
            throw new MemberValidationException("변경하려는 이름이 현재 이름과 동일합니다.");
        }

        this.name = changeName;
    }

    /**
     * 비밀번호 변경
     * @param encode 변경할 비밀번호
     */
    public void updatePassword(String encode) {
        if(encode == null || encode.isBlank()) {
            throw new MemberValidationException("비밀번호는 필수 사항입니다.");
        }

        if(encode.length() < 60) {
            throw new IllegalArgumentException("비밀번호 암호화의 자리 수 확인 필요");
        }

        this.password = encode;
    }

    public void retire() {
        if(this.retired) {
            throw new MemberValidationException("이미 탈퇴한 회원입니다.");
        }

        this.retired = true;
        this.name = "Leave " + this.role.name();
        this.password = UUID.randomUUID().toString();
    }
}