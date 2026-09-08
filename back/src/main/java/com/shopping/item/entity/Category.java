package com.shopping.item.entity;

import com.shopping.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class Category extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Category parent;

    @Builder.Default
    @OneToMany(mappedBy = "parent")
    private List<Category> children = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "category")
    private List<Item> items = new ArrayList<>();

    /**
     * 카테고리 연관관계 메소드
     * @param child 하위 카테고리
     */
    public void addChildCategory(Category child) {
        this.children.add(child);
        child.parent = this;
    }

    /**
     * 상품 연관관계 메소드
     * @param item 상품
     */
    public void addItem(Item item) {
        Category oldCategory = item.getCategory();

        if (oldCategory == this) {
            return;
        }

        if (oldCategory != null) {
            oldCategory.items.remove(item);
        }

        this.items.add(item);
        item.setCategory(this);
    }

    /**
     * 카테고리 생성 메소드
     * @param name 카테고리 이름
     * @return Category Entity
     */
    public static Category create(String name) {
        validateName(name);

        return Category.builder()
                .name(name)
                .build();
    }

    /**
     * 상위 카테고리 업데이트 메소드
     * @param newParent 상위 카테고리
     */
    public void changeParent(Category newParent) {
        if(this.parent == newParent) {
            return;
        }

        // 자기 자신이나 자신의 하위 카테고리를 부모로 설정하는 순환 방지
        for (Category current = newParent; current != null; current = current.parent) {
            if(current == this) {
                throw new IllegalArgumentException("자기 자신이나 하위 카테고리를 부모로 설정할 수 없습니다.");
            }
        }

        // 기존 부모와의 관계 제거
        if(this.parent != null) {
            this.parent.children.remove(this);
        }

        // 새 부모 설정
        this.parent = newParent;

        // 새 부모의 자식 목록에 추가
        if(newParent != null && !newParent.children.contains(this)) {
            newParent.children.add(this);
        }
    }

    /**
     * 상품 업데이트 메소드
     * @param item 상품
     */
    public void changeItem(Item item) {
        if(this.items.contains(item)) {
            return;
        }

        this.addItem(item);
    }

    /**
     * 카테고리 이름 업데이트 메소드
     * @param name 카테고리 이름
     */
    public void updateName(String name) {
        validateName(name);

        this.name = name;
    }

    /**
     * 상품 제거 메소드
     * @param item 상품
     */
    public void removeItem(Item item) {
        if(items.remove(item)) {
            item.removeCategory();
        }
    }

    /**
     * 상위 카테고리 삭제
     * <br>
     * 하위 카테고리가 있을 경우 삭제 불가능
     */
    public void removeCategory() {
        if(!this.getChildren().isEmpty()) {
            throw new IllegalArgumentException("하위 카테고리가 존재합니다.");
        }

        if(this.parent == null) {
            return;
        }

        this.parent.getChildren().remove(this);
        this.parent = null;
    }

    /**
     * 카테고리 이름 유효성 검사 메소드
     * @param name 카테고리 이름
     */
    private static void validateName(String name) {
        if(name == null || name.isBlank()) {
            throw new IllegalArgumentException("카테고리 이름은 비어있을 수 없습니다.");
        }
    }
}