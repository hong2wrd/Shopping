package com.shopping.item.entity;

import com.shopping.common.entity.BaseEntity;
import com.shopping.item.exception.InsufficientStockException;
import com.shopping.item.exception.InvalidItemValueException;
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

    protected Item(String name, int price, int stockQuantity) {
        validate(name, price);

        this.name = name;
        this.price = price;
        this.stockQuantity = stockQuantity;
    }

    /**
     * 상품 정보 수정
     */
    public void updateInfo(String name, int price) {
        validate(name, price);

        this.name = name;
        this.price = price;
    }

    /**
     * 재고 변뎡
     * @param stockQuantity
     */
    protected void adjustStock(int stockQuantity) {
        if (stockQuantity < 0) {
            throw new InvalidItemValueException("재고는 0보다 작을 수 없습니다.");
        }

        this.stockQuantity = stockQuantity;
    }

    /**
     * 재고 수량 증가
     */
    public void addStock(int quantity) {
        if(quantity < 0) {
            throw new InvalidItemValueException("재고 수량은 0보다 작을 수 없습니다.");
        }
        this.stockQuantity += quantity;
    }

    /**
     * 재고 수량 감소
     */
    public void removeStock(int quantity) {
        if(quantity < 0) {
            throw new InvalidItemValueException("재고 수량은 0보다 작을 수 없습니다.");
        }

        if (stockQuantity - quantity < 0) {
            throw new InsufficientStockException("재고가 부족합니다.");
        }

        this.stockQuantity -= quantity;
    }

    private void validate(String name, int price) {
        if (name == null || name.isBlank()) {
            throw new InvalidItemValueException("상품명은 필수입니다.");
        }

        if (price <= 0) {
            throw new InvalidItemValueException("가격은 0원보다 커야 합니다.");
        }
    }
}