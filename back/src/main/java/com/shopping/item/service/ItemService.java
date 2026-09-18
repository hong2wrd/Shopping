package com.shopping.item.service;

import com.shopping.common.exception.BusinessException;
import com.shopping.common.exception.ErrorCode;
import com.shopping.item.dto.request.ItemSaveReqDto;
import com.shopping.item.dto.request.ItemUpdateReqDto;
import com.shopping.item.dto.response.ItemRespDto;
import com.shopping.item.entity.clothes.Bottom;
import com.shopping.item.entity.clothes.Outer;
import com.shopping.item.entity.clothes.Shoes;
import com.shopping.item.entity.culture.Book;
import com.shopping.item.entity.Item;
import com.shopping.item.entity.clothes.Top;
import com.shopping.item.mapper.ItemMapper;
import com.shopping.item.repository.ItemRepository;
import com.shopping.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;

    /**
     * 상품 조회
     * @param itemId 조회할 상품 ID
     * @return 상품 DTO
     */
    @Transactional(readOnly = true)
    public ItemRespDto searchItem(Long itemId) {

        Item findItem = itemRepository.findByIdAndDeletedFalse(itemId)
                .orElseThrow(() -> new BusinessException(ErrorCode.ITEM_NOT_FOUND));

        if(findItem instanceof Book book) {
            return ItemMapper.toDto(book);
        } else if(findItem instanceof Top top) {
            return ItemMapper.toDto(top);
        } else if(findItem instanceof Bottom bottom) {
            return ItemMapper.toDto(bottom);
        } else if(findItem instanceof Outer outer) {
            return ItemMapper.toDto(outer);
        } else if(findItem instanceof Shoes shoes) {
            return ItemMapper.toDto(shoes);
        }

        throw new BusinessException(ErrorCode.ITEM_NOT_SUPPORTED);
    }

    /**
     * 상품을 저장합니다.
     */
    public ItemRespDto saveItem(String memId, ItemSaveReqDto itemSaveReqDto) {
        memberRepository.findByMemIdAndRetiredIsFalse(memId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEM_NOT_FOUND));

        Item item = createItem(itemSaveReqDto);

        Item saveItem = itemRepository.save(item);

        return ItemMapper.toDto(saveItem);
    }

    /**
     * Item 객체 생성
     * @param itemSaveReqDto 상품 저장 정보
     * @return
     */
    private Item createItem(ItemSaveReqDto itemSaveReqDto) {
        return switch (itemSaveReqDto.getItemtype()) {
            case BOOK -> Book.create(
                    itemSaveReqDto.getPrice(),
                    itemSaveReqDto.getQuantity(),
                    itemSaveReqDto.getName(),
                    itemSaveReqDto.getAuthor(),
                    itemSaveReqDto.getContent());
            case TOP -> Top.create(
                    itemSaveReqDto.getPrice(),
                    itemSaveReqDto.getQuantity(),
                    itemSaveReqDto.getName(),
                    itemSaveReqDto.getSize(),
                    itemSaveReqDto.getContent());
            case BOTTOM -> Bottom.create(
                    itemSaveReqDto.getPrice(),
                    itemSaveReqDto.getQuantity(),
                    itemSaveReqDto.getName(),
                    itemSaveReqDto.getSize(),
                    itemSaveReqDto.getContent());
            case OUTER -> Outer.create(
                    itemSaveReqDto.getPrice(),
                    itemSaveReqDto.getQuantity(),
                    itemSaveReqDto.getName(),
                    itemSaveReqDto.getSize(),
                    itemSaveReqDto.getContent());
            case SHOES -> Shoes.create(
                    itemSaveReqDto.getPrice(),
                    itemSaveReqDto.getQuantity(),
                    itemSaveReqDto.getName(),
                    itemSaveReqDto.getSize(),
                    itemSaveReqDto.getContent());
            default -> throw new BusinessException(ErrorCode.ITEM_NOT_SUPPORTED);
        };
    }

    /**
     * 상품을 수정합니다.
     */
    public ItemRespDto updateItem(String memId, ItemUpdateReqDto itemUpdateReqDto) {
        memberRepository.findByMemIdAndRetiredIsFalse(memId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEM_NOT_FOUND));

        Item findItem = itemRepository.findByIdForUpdate(itemUpdateReqDto.getItemId())
                .orElseThrow(() -> new BusinessException(ErrorCode.ITEM_NOT_FOUND));

        if(findItem instanceof Book book) {
            book.updateInfo(itemUpdateReqDto.getName(), itemUpdateReqDto.getPrice(), itemUpdateReqDto.getAuthor(), itemUpdateReqDto.getContent());
        } else if(findItem instanceof Top top) {
            top.updateInfo(itemUpdateReqDto.getName(), itemUpdateReqDto.getPrice(), itemUpdateReqDto.getSize(), itemUpdateReqDto.getContent());
        } else if(findItem instanceof Bottom bottom) {
            bottom.updateInfo(itemUpdateReqDto.getName(), itemUpdateReqDto.getPrice(), itemUpdateReqDto.getSize(), itemUpdateReqDto.getContent());
        } else if(findItem instanceof Outer outer) {
            outer.updateInfo(itemUpdateReqDto.getName(), itemUpdateReqDto.getPrice(), itemUpdateReqDto.getSize(), itemUpdateReqDto.getContent());
        } else if(findItem instanceof Shoes shoes) {
            shoes.updateInfo(itemUpdateReqDto.getName(), itemUpdateReqDto.getPrice(), itemUpdateReqDto.getSize(), itemUpdateReqDto.getContent());
        }
        return ItemMapper.toDto(findItem);
    }

    /**
     * 상품을 삭제합니다.
     * @param itemId 삭제할 상품 ID
     */
    public void deleteItem(String memId, Long itemId) {
        memberRepository.findByMemIdAndRetiredIsFalse(memId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEM_NOT_FOUND));

        Item findItem = itemRepository.findByIdForUpdate(itemId)
                .orElseThrow(() -> new BusinessException(ErrorCode.ITEM_NOT_FOUND));

        findItem.delete();
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
    public Item decreaseStock(Long itemId, int quantity) {
        Item findItem = itemRepository.findByIdForUpdate(itemId)
                .orElseThrow(() -> new BusinessException(ErrorCode.ITEM_NOT_FOUND));

        findItem.decreaseStock(quantity);

        return findItem;
    }
}
