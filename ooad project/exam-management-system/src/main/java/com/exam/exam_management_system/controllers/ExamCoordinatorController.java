package com.exam.exam_management_system.controllers;

import com.exam.exam_management_system.models.*;
import com.exam.exam_management_system.services.ExamService;
import com.exam.exam_management_system.facades.ExamRegistrationFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/coordinator")
public class ExamCoordinatorController extends BaseController {

    @Autowired
    private ExamService examService;

    @Autowired
    private ExamRegistrationFacade registrationFacade;

    @GetMapping("/schedules")
    public String showSchedules(Model model, HttpSession session) {
        addCommonAttributes(model, session);
        User currentUser = getCurrentUser(session);
        if (!(currentUser instanceof ExamCoordinator)) {
            return redirectToLogin();
        }

        List<ExamRegistration> registrations = examService.getAllRegistrations();
        model.addAttribute("registrations", registrations);
        return "coordinator/manage-schedules";
    }

    @PostMapping("/schedules/update")
    public String updateSchedules(
            @RequestParam("registrationIds") String registrationIdsStr,
            @RequestParam("newStartTime") LocalDateTime newStartTime,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        try {
            User currentUser = getCurrentUser(session);
            if (!(currentUser instanceof ExamCoordinator)) {
                return redirectToLogin();
            }

            // Convert comma-separated string to list of Longs
            List<Long> registrationIds = Arrays.stream(registrationIdsStr.split(","))
                    .map(Long::parseLong)
                    .collect(Collectors.toList());

            registrationFacade.updateSchedules(registrationIds, newStartTime, (ExamCoordinator) currentUser);
            redirectAttributes.addFlashAttribute("success", "Schedules updated successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to update schedules: " + e.getMessage());
        }
        return "redirect:/coordinator/schedules";
    }
} 