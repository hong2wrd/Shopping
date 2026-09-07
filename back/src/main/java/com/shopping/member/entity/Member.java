package com.shopping.member.entity;

import com.shopping.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

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
    private String name;

    private Role role;

    @Builder.Default
    private boolean retired = false;

    public static Member of(String memId, String name, String role) {
        return Member.builder()
                .memId(memId)
                .name(name)
                .role(Role.valueOf(role))
                .build();
    }

}