package com.enterprise.workflow.repository;

import com.enterprise.workflow.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUserIdAndReadStatusFalse(Long userId);
    List<Notification> findByUserIdOrderByCreatedAtDesc(Long userId);
}