package com.exam.exam_management_system.controllers;

import com.exam.exam_management_system.models.Exam;
import com.exam.exam_management_system.models.User;
import com.exam.exam_management_system.services.ExamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/student/exam")
public class StudentExamController extends BaseController {

    @Autowired
    private ExamService examService;

    @GetMapping
    public String listAvailableExams(Model model, HttpSession session) {
        addCommonAttributes(model, session);
        User currentUser = getCurrentUser(session);
        model.addAttribute("exams", examService.getAvailableExams(currentUser));
        return "student/exams";
    }

    @GetMapping("/{id}/view")
    public String viewExamDetails(@PathVariable Long id, Model model, HttpSession session) {
        addCommonAttributes(model, session);
        Exam exam = examService.getExamById(id);
        model.addAttribute("exam", exam);
        return "student/exam-details";
    }

    @GetMapping("/upcoming")
    public String viewUpcomingExams(Model model, HttpSession session) {
        addCommonAttributes(model, session);
        User currentUser = getCurrentUser(session);
        model.addAttribute("exams", examService.getUpcomingExams(currentUser));
        return "student/upcoming-exams";
    }
} 