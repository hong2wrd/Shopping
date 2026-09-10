package com.shopping.item.entity.clothes;

import com.shopping.item.entity.Item;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@DiscriminatorValue("SHOES")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder(access = AccessLevel.PRIVATE)
public class Shoes extends Item {

    @Column
    @Enumerated(EnumType.STRING)
    private Size size;

    protected Shoes(String name, int price, int stockQuantity, Size size) {
        super(name, price, stockQuantity);
        validateSize(size);
        this.size = size;
    }

    /**
     * Shoes(하의) 생성
     * @param price
     * @param stockQuantity
     * @param name
     * @param size
     * @return
     */
    public static Shoes create(int price, int stockQuantity, String name, Size size) {
        return new Shoes(name, price, stockQuantity, size);
    }

    public void updateInfo(String name, int price, Size size) {
        validateSize(size);
        super.updateInfo(name, price);
        this.size = size;
    }
}
