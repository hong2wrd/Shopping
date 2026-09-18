package com.shopping.item.entity;

import com.shopping.common.entity.BaseEntity;
import com.shopping.item.entity.clothes.Size;
import com.shopping.item.exception.InsufficientStockException;
import com.shopping.item.exception.InvalidItemValueException;
import jakarta.persistence.*;
import lombok.*;

/**
 * 상품 엔티티
 */
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

    @Column
    private String content;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false)
    private int stockQuantity;

    private boolean deleted;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    /**
     * 상품 생성
     * @param name 상품 이름
     * @param price 상품 가격
     * @param stockQuantity 상품 재고 수량
     */
    protected Item(String name, int price, int stockQuantity, String content) {
        validate(name, price);

        this.name = name;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.content = content;
        this.deleted = false;
    }

    /**
     * 상품 정보 수정
     * @param name 변경할 상품 이름
     * @param price 변경할 상품 가격
     */
    public void updateInfo(String name, int price, String content) {
        validate(name, price);

        this.name = name;
        this.price = price;
        this.content = content;
    }

    /**
     * 재고 변경
     * @param stockQuantity 대체할 재고 수량
     */
    public void adjustStock(int stockQuantity) {
        if (stockQuantity < 0) {
            throw new InvalidItemValueException("재고는 0보다 작을 수 없습니다.");
        }

        this.stockQuantity = stockQuantity;
    }

    /**
     * 재고 수량 증가
     * @param quantity 증가할 재고 수량
     */
    public void addStock(int quantity) {
        if(quantity < 0) {
            throw new InvalidItemValueException("재고 수량은 0보다 작을 수 없습니다.");
        }
        this.stockQuantity += quantity;
    }

    /**
     * 재고 수량 감소
     * @param quantity 감소할 재고 수량
     */
    public void decreaseStock(int quantity) {
        if(quantity < 0) {
            throw new InvalidItemValueException("재고 수량은 0보다 작을 수 없습니다.");
        }

        if (stockQuantity - quantity < 0) {
            throw new InsufficientStockException("재고가 부족합니다.");
        }

        this.stockQuantity -= quantity;
    }

    /**
     * 상품 정보 유효성 검증
     * @param name 변경할 상품 이름
     * @param price 변경할 상품 가격
     */
    private void validate(String name, int price) {
        if (name == null || name.isBlank()) {
            throw new InvalidItemValueException("상품명은 필수입니다.");
        }

        if (price <= 0) {
            throw new InvalidItemValueException("가격은 0원보다 커야 합니다.");
        }
    }

    /**
     * 카테고리 제거
     */
    public void removeCategory() {
        this.category = null;
    }

    public void delete() {
        this.deleted = true;
    }

    protected void validateSize(Size size) {
        if (size == null) {
            throw new InvalidItemValueException("사이즈는 필수입니다.");
        }
    }
}