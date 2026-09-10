package com.shopping.item.entity.clothes;

import com.shopping.item.entity.Item;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@DiscriminatorValue("OUTER")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder(access = AccessLevel.PRIVATE)
public class Outer extends Item {

    @Column
    @Enumerated(EnumType.STRING)
    private Size size;

    protected Outer(String name, int price, int stockQuantity, Size size) {
        super(name, price, stockQuantity);

        this.size = size;
    }

    /**
     * Outer(겉옷) 생성
     * @param price
     * @param stockQuantity
     * @param name
     * @param size
     * @return
     */
    public static Outer create(int price, int stockQuantity, String name, Size size) {
        return new Outer(name, price, stockQuantity, size);
    }

    public void updateInfo(String name, int price, Size size) {
        super.updateInfo(name, price);

        this.size = size;
    }
}
