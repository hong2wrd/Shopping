package com.shopping.item.entity.clothes;

import com.shopping.item.entity.Item;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@DiscriminatorValue("BOTTOM")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder(access = AccessLevel.PRIVATE)
public class Bottom extends Item {

    @Column
    @Enumerated(EnumType.STRING)
    private Size size;

    protected Bottom(String name, int price, int stockQuantity, Size size) {
        super(name, price, stockQuantity);

        this.size = size;
    }

    /**
     * Bottom(하의) 생성
     * @param price
     * @param stockQuantity
     * @param name
     * @param size
     * @return
     */
    public static Bottom create(int price, int stockQuantity, String name, Size size) {
        return new Bottom(name, price, stockQuantity, size);
    }


    public void updateInfo(String name, int price, Size size) {
        super.updateInfo(name, price);

        this.size = size;
    }
}
