package com.shopping.item.service;

import com.shopping.common.exception.BusinessException;
import com.shopping.common.exception.ErrorCode;
import com.shopping.item.dto.request.ItemSaveReqDto;
import com.shopping.item.dto.request.ItemUpdateReqDto;
import com.shopping.item.dto.response.ItemRespDto;
import com.shopping.item.entity.culture.Book;
import com.shopping.item.entity.Item;
import com.shopping.item.entity.clothes.Top;
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

    /**
     * 상품 조회
     * @param itemId 조회할 상품 ID
     * @return 상품 DTO
     */
    @Transactional(readOnly = true)
    public ItemRespDto searchItem(Long itemId) {

        Item findItem = itemRepository.findById(itemId)
                .orElseThrow(() -> new BusinessException(ErrorCode.ITEM_NOT_FOUND));

        if(findItem instanceof Book book) {
            return ItemMapper.toDto(book);
        }

        throw new BusinessException(ErrorCode.ITEM_NOT_SUPPORTED);
    }

    /**
     * 상품을 저장합니다.
     */
    public ItemRespDto saveItem(ItemSaveReqDto itemSaveReqDto) {
        Item item = switch (itemSaveReqDto.getItemtype()) {
            case BOOK -> Book.create(
                    itemSaveReqDto.getPrice(),
                    itemSaveReqDto.getQuantity(),
                    itemSaveReqDto.getName(),
                    itemSaveReqDto.getAuthor());
            case TOP -> Top.create(
                    itemSaveReqDto.getPrice(),
                    itemSaveReqDto.getQuantity(),
                    itemSaveReqDto.getName(),
                    itemSaveReqDto.getSize());
            default -> throw new BusinessException(ErrorCode.ITEM_NOT_SUPPORTED);
        };

        Item saveItem = itemRepository.save(item);
        if(saveItem instanceof Book) {
            return ItemMapper.toDto((Book) saveItem);
        }

        return ItemMapper.toDto(saveItem);
    }

    /**
     * 상품을 수정합니다.
     */
    public ItemRespDto updateItem(ItemUpdateReqDto totalItemSaveReqDto) {
        Item findItem = itemRepository.findByIdForUpdate(totalItemSaveReqDto.getItemId())
                .orElseThrow(() -> new BusinessException(ErrorCode.ITEM_NOT_FOUND));

        if(findItem instanceof Book book) {
            book.updateInfo(totalItemSaveReqDto.getName(), totalItemSaveReqDto.getPrice());
            return ItemMapper.toDto(book);
        }

        return ItemMapper.toDto(findItem);
    }

    /**
     * 상품을 삭제합니다.
     * @param itemId 삭제할 상품 ID
     */
    public void deleteItem(Long itemId) {
        Item findItem = itemRepository.findByIdForUpdate(itemId)
                .orElseThrow(() -> new BusinessException(ErrorCode.ITEM_NOT_FOUND));

        itemRepository.delete(findItem);
    }

    /**
     * 상품 수량을 추가한다.
     * @param itemId Item 테이블 PK
     * @param quantity 수량
     */
    public void addStock(Long itemId, int quantity) {
        Item findItem = itemRepository.findByIdForUpdate(itemId)
                .orElseThrow(() -> new BusinessException(ErrorCode.ITEM_NOT_FOUND));

        findItem.addStock(quantity);
    }

    /**
     * 상품 수량을 감소시킨다.
     * @param itemId Item 테이블 PK
     * @param quantity 수량
     */
    public void removeStock(Long itemId, int quantity) {
        Item findItem = itemRepository.findByIdForUpdate(itemId)
                .orElseThrow(() -> new BusinessException(ErrorCode.ITEM_NOT_FOUND));

        findItem.removeStock(quantity);
    }
}
