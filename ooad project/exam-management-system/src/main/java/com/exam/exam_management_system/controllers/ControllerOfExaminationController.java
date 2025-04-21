package com.exam.exam_management_system.controllers;

import com.exam.exam_management_system.models.ControllerOfExamination;
import com.exam.exam_management_system.models.Exam;
import com.exam.exam_management_system.models.User;
import com.exam.exam_management_system.models.enums.ExamState;
import com.exam.exam_management_system.services.ExamService;
import com.exam.exam_management_system.commands.PaperCommandInvoker;
import com.exam.exam_management_system.commands.SetPaperOnlineCommand;
import com.exam.exam_management_system.commands.SetPaperOfflineCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/coe")
public class ControllerOfExaminationController extends BaseController {

    @Autowired
    private ExamService examService;

    @GetMapping("/dashboard")
    public String showDashboard(Model model, HttpSession session) {
        User currentUser = getCurrentUser(session);
        if (!(currentUser instanceof ControllerOfExamination)) {
            return redirectToLogin();
        }

        addCommonAttributes(model, session);
        return "dashboards/controller-dashboard";
    }

    @GetMapping("/stored-papers")
    public String viewStoredPapers(Model model, HttpSession session) {
        User currentUser = getCurrentUser(session);
        if (!(currentUser instanceof ControllerOfExamination)) {
            return redirectToLogin();
        }

        List<Exam> storedPapers = examService.getStoredExamPapers();
        model.addAttribute("storedPapers", storedPapers.stream()
            .filter(exam -> exam.getState() == ExamState.STORED_IN_COE)
            .collect(Collectors.toList()));
        addCommonAttributes(model, session);
        
        return "coe/stored-papers";
    }

    @GetMapping("/active-papers")
    public String viewActivePapers(Model model, HttpSession session) {
        User currentUser = getCurrentUser(session);
        if (!(currentUser instanceof ControllerOfExamination)) {
            return redirectToLogin();
        }

        List<Exam> activePapers = examService.getActiveOnlineExams();
        model.addAttribute("activePapers", activePapers.stream()
            .filter(exam -> exam.getState() == ExamState.ACTIVE)
            .collect(Collectors.toList()));
        addCommonAttributes(model, session);
        
        return "coe/active-papers";
    }

    @PostMapping("/papers/{id}/set-online")
    public String setPaperOnline(@PathVariable Long id, HttpSession session, RedirectAttributes redirectAttributes) {
        User currentUser = getCurrentUser(session);
        if (!(currentUser instanceof ControllerOfExamination)) {
            return redirectToLogin();
        }

        try {
            Exam exam = examService.getExamById(id);
            PaperCommandInvoker invoker = new PaperCommandInvoker();
            invoker.executeCommand(new SetPaperOnlineCommand(exam, examService));
            redirectAttributes.addFlashAttribute("success", "Exam paper has been set to online mode.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to set paper online: " + e.getMessage());
        }

        return "redirect:/coe/stored-papers";
    }

    @PostMapping("/papers/{id}/set-offline")
    public String setPaperOffline(@PathVariable Long id, HttpSession session, RedirectAttributes redirectAttributes) {
        User currentUser = getCurrentUser(session);
        if (!(currentUser instanceof ControllerOfExamination)) {
            return redirectToLogin();
        }

        try {
            Exam exam = examService.getExamById(id);
            PaperCommandInvoker invoker = new PaperCommandInvoker();
            invoker.executeCommand(new SetPaperOfflineCommand(exam, examService));
            redirectAttributes.addFlashAttribute("success", "Exam paper has been set to offline mode.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to set paper offline: " + e.getMessage());
        }

        return "redirect:/coe/stored-papers";
    }
} 