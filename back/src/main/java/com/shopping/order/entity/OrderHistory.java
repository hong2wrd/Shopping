package com.shopping.order.entity;

import com.shopping.common.entity.BaseEntity;
import com.shopping.item.entity.Item;
import com.shopping.member.entity.Member;
import com.shopping.order.exception.OrderValidationException;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Getter
@Builder(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicUpdate
public class OrderHistory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private int price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public static OrderHistory create(Item item, int quantity, int price, Order order, Member member) {
        validation(item, quantity, price, order, member);

        return OrderHistory.builder()
                .item(item)
                .quantity(quantity)
                .price(price)
                .order(order)
                .member(member)
                .build();
    }

    /**
     * 상품이력에 대한 유효성 검사
     * @param item 상품
     * @param quantity 수량
     * @param price 가격
     * @param order 주문
     * @param member 회원
     */
    private static void validation(Item item, int quantity, int price, Order order, Member member) {
        if (item == null) {
            throw new OrderValidationException("상품 정보는 필수 사항입니다.");
        }

        if (quantity <= 0) {
            throw new OrderValidationException("상품 수량은 필수 사항입니다.");
        }

        if (price <= 0) {
            throw new OrderValidationException("상품 가격은 필수 사항입니다.");
        }

        if (order == null) {
            throw new OrderValidationException("주문 정보는 필수 사항입니다.");
        }

        if (member == null) {
            throw new OrderValidationException("회원 정보는 필수 사항입니다.");
        }
    }
}