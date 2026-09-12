package com.shopping.order.service;

import com.shopping.common.exception.BusinessException;
import com.shopping.common.exception.ErrorCode;
import com.shopping.item.entity.Item;
import com.shopping.item.repository.ItemRepository;
import com.shopping.member.entity.Member;
import com.shopping.member.repository.MemberRepository;
import com.shopping.order.dto.request.OrderCreateReqDto;
import com.shopping.order.dto.response.OrderCreateRespDto;
import com.shopping.order.entity.Order;
import com.shopping.order.entity.OrderHistory;
import com.shopping.order.repository.OrderHistoryRepository;
import com.shopping.order.repository.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderHistoryRepository orderHistoryRepository;
    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;

    public OrderCreateRespDto saveOrder(OrderCreateReqDto orderCreateReqDto, String memId) {

        Member findMember = memberRepository.findByMemIdAndRetiredIsFalse(memId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEM_NOT_FOUND));

        Order order = orderRepository.save(Order.create(
                orderCreateReqDto.getTotalPrice(),
                orderCreateReqDto.getTotalDiscountPrice(),
                orderCreateReqDto.getTotalDiscountRate()));

        List<OrderHistory> orderHistories = orderCreateReqDto.getOrderItems()
                .stream()
                .map(dto -> {
                    Item findItem = itemRepository.findByIdAndDeletedFalse(dto.getItemId())
                            .orElseThrow(() -> new BusinessException(ErrorCode.ITEM_NOT_FOUND));

                    return OrderHistory.create(findItem, dto.getQuantity(), dto.getPrice(), order, findMember);
                }).toList();
        orderHistoryRepository.saveAll(orderHistories);

        return new OrderCreateRespDto(order.getId());
    }
}
