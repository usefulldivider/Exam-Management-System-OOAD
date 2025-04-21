package com.exam.exam_management_system.controllers;

import com.exam.exam_management_system.models.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import jakarta.servlet.http.HttpSession;

@Controller
public class DashboardController extends BaseController {

    @GetMapping("/dashboard")
    public String showDashboard(Model model, HttpSession session) {
        addCommonAttributes(model, session);
        User currentUser = getCurrentUser(session);

        if (currentUser == null) {
            return redirectToLogin();
        }

        switch (currentUser.getRole()) {
            case EXAM_COORDINATOR:
                return "dashboards/coordinator-dashboard";
            case CONTROLLER_OF_EXAMINATION:
                return "dashboards/controller-dashboard";
            case TEACHER:
                return "dashboards/teacher-dashboard";
            case STUDENT:
                return "dashboards/student-dashboard";
            default:
                return redirectToLogin();
        }
    }
} 