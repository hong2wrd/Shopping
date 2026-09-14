package com.shopping.order.repository;

import com.shopping.order.dto.response.OrderSearchRespDto;
import com.shopping.order.entity.Order;
import com.shopping.order.entity.OrderHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderHistoryRepository extends JpaRepository<OrderHistory, Long> {

    /**
     * 주문 내역 조회
     * @param order 주문
     * @return 주문 검색 응답 DTO 리스트
     */
    @Query("""
                SELECT new com.shopping.order.dto.response.OrderSearchRespDto(
                    oh.item.id,
                    oh.item.name,
                    oh.price,
                    oh.quantity
                    )
                  FROM OrderHistory oh
                 WHERE oh.order = :order
            """)
    List<OrderSearchRespDto> findOrderHistoriesByOrder(Order order);
}
