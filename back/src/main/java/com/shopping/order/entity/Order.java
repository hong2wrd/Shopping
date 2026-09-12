package com.shopping.order.entity;

import com.shopping.common.entity.BaseEntity;
import com.shopping.member.entity.Member;
import com.shopping.order.exception.OrderValidationException;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Getter
@Builder(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicUpdate
@Table(name = "orders")
public class Order extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(value = EnumType.STRING)
    private OrderStatus status;

    @Column(nullable = false)
    private int totalPrice;

    @Column(nullable = false)
    private int totalDiscountPrice;

    @Column(nullable = false)
    private double totalDiscountRate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    /**
     * 주문 생성
     * @param totalPrice 총 금액
     * @param totalDiscountPrice 총 할인 금액
     * @param totalDiscountRate 총 할인 비율
     * @return
     */
    public static Order create(int totalPrice, int totalDiscountPrice, double totalDiscountRate) {
        if(totalPrice < 0) {
            throw new OrderValidationException("주문 금액은 0원 이상이어야 합니다.");
        }

        if(totalDiscountPrice < 0) {
            throw new OrderValidationException("주문 할인 금액은 0원 이상이어야 합니다.");
        }

        if(totalDiscountRate < 0) {
            throw new OrderValidationException("주문 할인 비율은 0 이상이어야 합니다.");
        }

        return Order.builder()
                .totalPrice(totalPrice)
                .build();
    }
}