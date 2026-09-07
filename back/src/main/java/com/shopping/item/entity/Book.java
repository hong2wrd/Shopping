package com.shopping.item.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.*;

@Entity
@Getter
@DiscriminatorValue("BOOK")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder(access = AccessLevel.PRIVATE)
public class Book extends Item {

    private String author;

    /**
     * 도서를 생성합니다.
     *
     * @param price      가격
     * @param stockQuantity 재고 수량
     * @param name       이름
     * @param author     저자
     * @return 생성된 도서
     */
    public static Book create(int price, int stockQuantity, String name, String author) {

        Book book = Book.builder()
                .author(author)
                .build();

        book.update(name, price, stockQuantity);

        return book;
    }

}
