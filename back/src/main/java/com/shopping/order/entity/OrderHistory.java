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
    private static void validation(Item item, int quantity, int price, Order order, Member member) {
        if (item == null) {
            throw new OrderValidationException("상품 정보는 필수 사항입니다.");
        }
        //TODO 유효성 검사
    }
}