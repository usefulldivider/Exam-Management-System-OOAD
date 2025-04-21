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
@RequestMapping("/teacher/exam")
public class TeacherExamController extends BaseController {

    @Autowired
    private ExamService examService;

    @GetMapping
    public String listExams(Model model, HttpSession session) {
        addCommonAttributes(model, session);
        User currentUser = getCurrentUser(session);
        model.addAttribute("exams", examService.getExamsForTeacher(currentUser));
        return "teacher/exams";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model, HttpSession session) {
        addCommonAttributes(model, session);
        model.addAttribute("exam", new Exam());
        return "teacher/exam-form";
    }

    @PostMapping("/create")
    public String createExam(@ModelAttribute Exam exam, HttpSession session) {
        User currentUser = getCurrentUser(session);
        examService.createExam(exam, currentUser);
        return "redirect:/teacher/exams";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model, HttpSession session) {
        addCommonAttributes(model, session);
        Exam exam = examService.getExamById(id);
        model.addAttribute("exam", exam);
        return "teacher/exam-form";
    }

    @PostMapping("/{id}/edit")
    public String updateExam(@PathVariable Long id, @ModelAttribute Exam exam, HttpSession session) {
        examService.updateExam(id, exam.getSubject(), exam.getExamDate(), exam.getDuration());
        return "redirect:/teacher/exams";
    }

    @GetMapping("/{id}/questions")
    public String manageQuestions(@PathVariable Long id, Model model, HttpSession session) {
        addCommonAttributes(model, session);
        Exam exam = examService.getExamById(id);
        model.addAttribute("exam", exam);
        return "teacher/exam-questions";
    }

    @GetMapping("/{id}/results")
    public String viewResults(@PathVariable Long id, Model model, HttpSession session) {
        addCommonAttributes(model, session);
        Exam exam = examService.getExamById(id);
        model.addAttribute("exam", exam);
        model.addAttribute("results", examService.getExamResults(id));
        return "exam-results";
    }

    @PostMapping("/{id}/publish")
    public String publishExam(@PathVariable Long id) {
        examService.publishExam(id);
        return "redirect:/teacher/exams";
    }
} 