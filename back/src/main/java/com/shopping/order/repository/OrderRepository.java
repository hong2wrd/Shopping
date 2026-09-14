package com.shopping.order.repository;

import com.shopping.member.entity.Member;
import com.shopping.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    Optional<Order> findByIdAndMember(Long orderId, Member member);

}
