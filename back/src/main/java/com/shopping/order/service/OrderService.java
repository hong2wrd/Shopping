package com.shopping.order.service;

import com.shopping.common.exception.BusinessException;
import com.shopping.common.exception.ErrorCode;
import com.shopping.item.entity.Item;
import com.shopping.item.repository.ItemRepository;
import com.shopping.member.entity.Member;
import com.shopping.member.repository.MemberRepository;
import com.shopping.order.dto.request.OrderCreateReqDto;
import com.shopping.order.dto.response.OrderCreateRespDto;
import com.shopping.order.dto.response.OrderSearchRespDto;
import com.shopping.order.entity.Order;
import com.shopping.order.entity.OrderHistory;
import com.shopping.order.repository.OrderHistoryRepository;
import com.shopping.order.repository.OrderRepository;
import com.shopping.payment.service.PaymentService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.IntStream;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderHistoryRepository orderHistoryRepository;
    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;
    private final PaymentService paymentService;

    /**
     * 주문 검색
     * @param orderNumber 주문 번호
     * @param memId 회원 ID
     * @return 주문 검색 응답 DTO 리스트
     */
    public List<OrderSearchRespDto> searchByOrderNumber(String orderNumber, String memId) {
        Order findOrder = orderRepository.findByOrderNumberAndMember_MemId(orderNumber, memId)
                .orElseThrow(() -> new BusinessException(ErrorCode.ORDER_NOT_FOUND));

        return orderHistoryRepository.findOrderHistoriesByOrder(findOrder);
    }

    /**
     * 주문 생성
     * @param orderCreateReqDto 주문 생성 정보 요청 DTO
     * @param memId 회원 ID
     * @return 주문 생성 응답 DTO
     */
    public OrderCreateRespDto saveOrder(OrderCreateReqDto orderCreateReqDto, String memId) {

        Member findMember = memberRepository.findByMemIdAndRetiredIsFalse(memId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEM_NOT_FOUND));

        validateOrder(orderCreateReqDto, findMember);

        // 주문 생성
        Order order = orderRepository.save(Order.create(
                orderCreateReqDto.getTotalPrice(),
                orderCreateReqDto.getTotalDiscountPrice(),
                orderCreateReqDto.getTotalDiscountRate(),
                findMember
                ));

        // 주문 이력 생성
        List<OrderHistory> orderHistories = orderCreateReqDto.getOrderItems()
                .stream()
                .map(dto -> {
                    Item findItem = itemRepository.findByIdAndDeletedFalse(dto.getItemId())
                            .orElseThrow(() -> new BusinessException(ErrorCode.ITEM_NOT_FOUND));
                    int discountAmount = findMember.getMemberShip().getDiscountAmount(findItem.getPrice());

                    return OrderHistory.create(findItem, dto.getQuantity(), discountAmount, order, findMember);
                })
                .toList();

        orderHistoryRepository.saveAll(orderHistories);

        String paymentAttemptId = paymentService.createPayment(order.getId(), findMember.getMemId());

        order.paymentComplete();

        return new OrderCreateRespDto(order.getOrderNumber(), paymentAttemptId);
    }

    /**
     * 주문가격이 일치하지 않을 경우 예외를 발생시킵니다.
     * @param orderCreateReqDto 주문 생성 요청 DTO
     */
    private void validateOrder(OrderCreateReqDto orderCreateReqDto, Member member) {
        int totalPrice = orderCreateReqDto.getOrderItems()
                .stream()
                .flatMapToInt(dto -> {
                    Item findItem = itemRepository.findByIdAndDeletedFalse(dto.getItemId())
                            .orElseThrow(() -> new BusinessException(ErrorCode.ITEM_NOT_FOUND));
                    return IntStream.of(member.getMemberShip().getDiscountAmount(findItem.getPrice()) * dto.getQuantity());
                })
                .sum();

        if (totalPrice != orderCreateReqDto.getTotalPrice()) {
            throw new BusinessException(ErrorCode.ORDER_PRICE_NOT_MATCH);
        }
    }

    /**
     * 주문 취소
     * @param orderId 주문 ID
     * @param attemptId 결제 시도 ID
     * @param memId 회원 ID
     */
    public void cancelOrder(Long orderId, String attemptId, String memId) {
        Order findOrder = orderRepository.findByIdAndMember_MemId(orderId, memId)
                .orElseThrow(() -> new BusinessException(ErrorCode.ORDER_NOT_FOUND));

        findOrder.orderCancel();

        paymentService.cancelPayment(attemptId, orderId, memId);
    }
}
