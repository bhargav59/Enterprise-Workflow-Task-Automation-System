package com.enterprise.workflow.service;

import com.enterprise.workflow.entity.Notification;
import com.enterprise.workflow.entity.User;
import com.enterprise.workflow.repository.NotificationRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class NotificationService {
    private final NotificationRepository notificationRepository;
    
    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }
    
    public Notification create(User user, String message) {
        Notification notification = new Notification();
        notification.setMessage(message);
        notification.setUser(user);
        return notificationRepository.save(notification);
    }
    
    public List<Notification> getUnreadByUser(Long userId) {
        return notificationRepository.findByUserIdAndReadStatusFalse(userId);
    }
    
    public List<Notification> getAllByUser(Long userId) {
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }
    
    public void markAsRead(Long id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found"));
        notification.setReadStatus(true);
        notificationRepository.save(notification);
    }
    
    public void markAllAsRead(Long userId) {
        List<Notification> notifications = getUnreadByUser(userId);
        for (Notification n : notifications) {
            n.setReadStatus(true);
            notificationRepository.save(n);
        }
    }
}