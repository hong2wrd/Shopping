package com.shopping.notification.entity;

import com.shopping.common.entity.BaseEntity;
import com.shopping.member.entity.Member;
import com.shopping.notification.exception.NotificationValidException;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Notification extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Message message;

    @Column(nullable = false)
    private String orderNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    /**
     * Notification 생성 메서드
     * @param message 알림 메시지
     * @param orderNumber 주문 번호
     * @return Notification 객체
     */
    public static Notification of(String orderNumber, Message message, Member member) {
        if(message == null) {
            throw new NotificationValidException("알림 메시지는 필수 입력값입니다.");
        }

        if(orderNumber == null || orderNumber.isBlank()) {
            throw new NotificationValidException("주문 번호는 필수 입력값입니다.");
        }

        if(member == null) {
            throw new NotificationValidException("회원 정보는 필수 입력값입니다.");
        }

        return Notification.builder()
                .message(message)
                .orderNumber(orderNumber)
                .member(member)
                .build();
    }
}
