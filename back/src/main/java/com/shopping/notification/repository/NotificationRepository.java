package com.shopping.notification.repository;

import com.shopping.notification.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    Optional<Notification> findByOrderNumberAndMember_MemId(String orderNumber, String memId);
}
