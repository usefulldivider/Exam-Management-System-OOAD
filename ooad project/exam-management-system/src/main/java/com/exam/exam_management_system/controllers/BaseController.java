// Architecture Pattern: MVC - Controller (Base Controller)
// Design Pattern: Template Method - Abstract base class defining common controller behavior

package com.exam.exam_management_system.controllers;

import com.exam.exam_management_system.models.User;
import com.exam.exam_management_system.services.NotificationService;
import com.exam.exam_management_system.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import jakarta.servlet.http.HttpSession;

// Design Pattern: Template Method - Abstract base class
public abstract class BaseController {
    
    // Architecture Pattern: Service Layer Pattern - Service injection
    @Autowired
    protected UserService userService;
    
    // Architecture Pattern: Service Layer Pattern - Service injection
    @Autowired
    protected NotificationService notificationService;
    
    // Design Pattern: Template Method - Common template method for all controllers
    protected void addCommonAttributes(Model model, HttpSession session) {
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser != null) {
            model.addAttribute("currentUser", currentUser);
            model.addAttribute("notifications", notificationService.getNotificationsForUser(currentUser));
        }
    }
    
    // Design Pattern: Template Method - Helper method for child classes
    protected User getCurrentUser(HttpSession session) {
        return (User) session.getAttribute("currentUser");
    }

    // Design Pattern: Template Method - Helper method for child classes
    protected boolean isAuthenticated(HttpSession session) {
        return session.getAttribute("currentUser") != null;
    }

    // Design Pattern: Template Method - Helper method for child classes
    protected String redirectToLogin() {
        return "redirect:/login";
    }
} 