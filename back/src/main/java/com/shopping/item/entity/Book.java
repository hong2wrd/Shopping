package com.shopping.item.entity;

import jakarta.persistence.Column;
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

    @Column(nullable = false)
    private String author;

    protected Book(String name, int price, int stockQuantity, String author) {
        super(name, price, stockQuantity);

        validateAuthor(author);

        this.author = author;
    }

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
        return new Book(name, price, stockQuantity, author);
    }

    public void updateInfo(String name, int price, int stockQuantity, String author) {
        super.updateInfo(name, price);

        validateAuthor(author);

        this.author = author;
    }

    public void adjustStock(int stockQuantity) {
        super.adjustStock(stockQuantity);

    }

    private void validateAuthor(String author) {
        if(author == null || author.isBlank()) {
            throw new IllegalArgumentException("저자명은 필수사항입니다.");
        }
    }

}
