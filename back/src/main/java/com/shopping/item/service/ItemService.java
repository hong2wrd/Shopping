package com.shopping.item.service;

import com.shopping.item.dto.request.TotalItemSaveReqDto;
import com.shopping.item.dto.request.TotalItemUpdateReqDto;
import com.shopping.item.dto.response.ItemRespDto;
import com.shopping.item.entity.Book;
import com.shopping.item.entity.Item;
import com.shopping.item.mapper.ItemMapper;
import com.shopping.item.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    @Transactional(readOnly = true)
    public ItemRespDto searchItem(Long itemId) {

        Item findItem = itemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("해당 상품이 없습니다."));

        if(findItem instanceof Book book) {
            return ItemMapper.toDto(book);
        }

        throw new IllegalStateException("지원하지 않는 상품 타입입니다.");
    }

    /**
     * 상품을 저장합니다.
     */
    public void saveItem(TotalItemSaveReqDto totalItemSaveReqDto) {
        Item item = switch (totalItemSaveReqDto.getItemtype()) {
            case BOOK -> Book.create(
                    totalItemSaveReqDto.getPrice(),
                    totalItemSaveReqDto.getStockQuantity(),
                    totalItemSaveReqDto.getName(),
                    totalItemSaveReqDto.getAuthor());
        };

        itemRepository.save(item);
    }

    /**
     * 상품을 수정합니다.
     */
    public void updateItem(TotalItemUpdateReqDto totalItemSaveReqDto) {
        Item findItem = itemRepository.findByIdForUpdate(totalItemSaveReqDto.getItemId())
                .orElseThrow(() -> new IllegalArgumentException("해당 상품이 없습니다."));

        findItem.update(
                totalItemSaveReqDto.getName(),
                totalItemSaveReqDto.getPrice(),
                totalItemSaveReqDto.getStockQuantity()
        );
    }

    /**
     * 상품을 삭제합니다.
     */
    public void deleteItem(Long itemId) {
        Item findItem = itemRepository.findByIdForUpdate(itemId)
                .orElseThrow(() -> new IllegalArgumentException("해당 상품이 없습니다."));

        itemRepository.delete(findItem);
    }

    /**
     * 상품 수량을 추가한다.
     * @param itemId Item 테이블 PK
     * @param quantity 수량
     */
    public void addStock(Long itemId, int quantity) {
        Item findItem = itemRepository.findByIdForUpdate(itemId)
                .orElseThrow(() -> new IllegalArgumentException("해당 상품이 없습니다."));

        findItem.addStock(quantity);
    }

    /**
     * 상품 수량을 감소시킨다.
     * @param itemId Item 테이블 PK
     * @param quantity 수량
     */
    public void removeStock(Long itemId, int quantity) {
        Item findItem = itemRepository.findByIdForUpdate(itemId)
                .orElseThrow(() -> new IllegalArgumentException("해당 상품이 없습니다."));

        findItem.removeStock(quantity);
    }
}
