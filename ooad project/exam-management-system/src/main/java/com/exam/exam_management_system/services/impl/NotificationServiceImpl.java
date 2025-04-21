package com.exam.exam_management_system.services.impl;

import com.exam.exam_management_system.models.User;
import com.exam.exam_management_system.services.NotificationService;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {
    @Override
    public void sendNotification(User user, String message) {
        // Placeholder implementation
        System.out.println("Notification sent to " + user.getEmail() + ": " + message);
    }

    @Override
    public List<String> getNotificationsForUser(User user) {
        // Placeholder implementation
        System.out.println("Retrieving notifications for " + user.getUsername() + " (placeholder)");
        return new ArrayList<>();
    }
} 