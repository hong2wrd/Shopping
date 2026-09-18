package com.shopping.item.entity.clothes;

import com.shopping.item.entity.Item;
import com.shopping.item.exception.InvalidItemValueException;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@DiscriminatorValue("TOP")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder(access = AccessLevel.PRIVATE)
public class Top extends Item {

    @Column
    @Enumerated(EnumType.STRING)
    private Size size;

    protected Top(String name, int price, int stockQuantity, Size size, String content) {
        super(name, price, stockQuantity, content);
        validateSize(size);
        this.size = size;
    }

    /**
     * Top(상의) 생성
     * @param price
     * @param stockQuantity
     * @param name
     * @param size
     * @return
     */
    public static Top create(int price, int stockQuantity, String name, Size size, String content) {
        return new Top(name, price, stockQuantity, size, content);
    }

    public void updateInfo(String name, int price, Size size, String content) {
        validateSize(size);
        super.updateInfo(name, price, content);
        this.size = size;
    }

}
