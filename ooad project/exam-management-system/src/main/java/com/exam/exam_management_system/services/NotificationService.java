package com.exam.exam_management_system.services;

import com.exam.exam_management_system.models.User;
import java.util.List;

public interface NotificationService {
    void sendNotification(User user, String message);
    List<String> getNotificationsForUser(User user);
} 