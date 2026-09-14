package com.shopping.order.repository;

import com.shopping.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("""
            SELECT o
            FROM Order o
            WHERE o.id = :orderId
              AND o.member.memId = :memId
    """)
    Optional<Order> findByOrderIdAndMemId(@Param("orderId") Long orderId,
                                          @Param("memId") String memId);

    @Query("""
            SELECT o
            FROM Order o
            WHERE o.orderNumber = :orderNumber
              AND o.member.memId = :memId
    """)
    Optional<Order> findByOrderNumberAndMemId(@Param("orderNumber") String orderNumber,
                                              @Param("memId") String memId);
}
