package com.shopping.item.entity.culture;

import com.shopping.item.entity.Item;
import com.shopping.item.exception.InvalidItemValueException;
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

    @Column
    private String author;

    protected Book(String name, int price, int stockQuantity, String author, String content) {
        super(name, price, stockQuantity, content);

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
    public static Book create(int price, int stockQuantity, String name, String author, String content) {
        return new Book(name, price, stockQuantity, author, content);
    }

    public void updateInfo(String name, int price, String author, String content) {
        validateAuthor(author);
        super.updateInfo(name, price, content);
        this.author = author;
    }

    private void validateAuthor(String author) {
        if(author == null || author.isBlank()) {
            throw new InvalidItemValueException("저자명은 필수사항입니다.");
        }
    }

}
