package com.exam.exam_management_system.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.exam.exam_management_system.services.ExamService;
import com.exam.exam_management_system.models.Student;
import com.exam.exam_management_system.models.ExamResult;
import com.exam.exam_management_system.models.User;

import jakarta.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/student")
public class ExamResultController extends BaseController {

    @Autowired
    private ExamService examService;

    @GetMapping("/all-exam-results")
    public String showAllExamResults(HttpSession session, Model model) {
        User user = getCurrentUser(session);
        if (user == null || !(user instanceof Student)) {
            return redirectToLogin();
        }
        Student student = (Student) user;
        List<ExamResult> examResults = examService.getExamResultsByStudent(student);
        model.addAttribute("examResults", examResults);
        return "student/all-exam-results";
    }
} 