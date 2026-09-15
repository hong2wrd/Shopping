package com.shopping.order.repository;

import com.shopping.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    Optional<Order> findByIdAndMember_MemId(Long orderId, String memId);

    Optional<Order> findByOrderNumberAndMember_MemId(String orderNumber, String memId);

}
