package com.shopping.item.entity;

import com.shopping.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")
public abstract class Item extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false)
    private int stockQuantity;

    @Column(name = "dtype", nullable = false, insertable = false, updatable = false)
    @Enumerated(EnumType.STRING)
    private ItemType dtype;

    /**
     * 상품 정보 수정
     */
    public void update(String name, int price, int stockQuantity) {
        if(price <= 0) {
            throw new IllegalArgumentException("가격은 0원보다 작을 수 없습니다.");
        }

        if(stockQuantity < 0) {
            throw new IllegalArgumentException("재고는 0보다 작을 수 없습니다.");
        }

        this.name = name;
        this.price = price;
        this.stockQuantity = stockQuantity;
    }

    /**
     * 재고 수량 증가
     */
    public void addStock(int quantity) {
        if(quantity < 0) {
            throw new IllegalArgumentException("재고 수량은 0보다 작을 수 없습니다.");
        }
        this.stockQuantity += quantity;
    }

    /**
     * 재고 수량 감소
     */
    public void removeStock(int quantity) {
        if(quantity < 0) {
            throw new IllegalArgumentException("재고 수량은 0보다 작을 수 없습니다.");
        }

        if (stockQuantity - quantity < 0) {
            throw new IllegalArgumentException("재고가 부족합니다.");
        }

        this.stockQuantity -= quantity;
    }
}