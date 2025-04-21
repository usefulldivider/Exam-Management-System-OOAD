package com.exam.exam_management_system.controllers;

import com.exam.exam_management_system.models.User;
import com.exam.exam_management_system.models.enums.*;
import com.exam.exam_management_system.services.UserService;
import com.exam.exam_management_system.services.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/coordinator/registrations")
public class RegistrationController extends BaseController {

    @Autowired
    private UserService userService;

    @Autowired
    private NotificationService notificationService;

    @GetMapping
    public String viewPendingRegistrations(Model model, HttpSession session) {
        if (!isAuthenticated(session) || !isCoordinator(session)) {
            return redirectToLogin();
        }

        addCommonAttributes(model, session);
        model.addAttribute("pendingRegistrations", userService.getPendingRegistrations());
        return "dashboards/registration-approvals";
    }

    @PostMapping("/{userId}/approve")
    public String approveRegistration(@PathVariable Long userId, HttpSession session) {
        if (!isAuthenticated(session) || !isCoordinator(session)) {
            return redirectToLogin();
        }

        User user = userService.getUserById(userId);
        if (user != null) {
            user.setStatus(AccountStatus.APPROVED);
            userService.updateUser(user);
            
            // Notify the user about approval
            notificationService.sendNotification(user, "Your registration has been approved. You can now log in.");
        }

        return "redirect:/coordinator/registrations";
    }

    @PostMapping("/{userId}/reject")
    public String rejectRegistration(@PathVariable Long userId, HttpSession session) {
        if (!isAuthenticated(session) || !isCoordinator(session)) {
            return redirectToLogin();
        }

        User user = userService.getUserById(userId);
        if (user != null) {
            user.setStatus(AccountStatus.REJECTED);
            userService.updateUser(user);
            
            // Notify the user about rejection
            notificationService.sendNotification(user, "Your registration has been rejected. Please contact the administration for more details.");
        }

        return "redirect:/coordinator/registrations";
    }

    private boolean isCoordinator(HttpSession session) {
        User user = getCurrentUser(session);
        return user != null && user.getRole() == UserRole.EXAM_COORDINATOR;
    }
}