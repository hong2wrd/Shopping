package com.shopping.notification.service;

import com.shopping.common.exception.BusinessException;
import com.shopping.common.exception.ErrorCode;
import com.shopping.member.entity.Member;
import com.shopping.member.repository.MemberRepository;
import com.shopping.notification.entity.Message;
import com.shopping.notification.entity.Notification;
import com.shopping.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final MemberRepository memberRepository;

    public void sendNotification(String orderNumber, Message message, String memId) {
        Member findMember = memberRepository.findByMemIdAndRetiredIsFalse(memId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEM_NOT_FOUND));

        Optional<Notification> findNotificationOpt = notificationRepository.findByOrderNumberAndMember_MemId(orderNumber, memId);
        if(findNotificationOpt.isPresent()) {
            return;
        }

        notificationRepository.save(Notification.of(orderNumber, message, findMember));
    }
}
