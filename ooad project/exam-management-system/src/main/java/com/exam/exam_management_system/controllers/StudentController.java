package com.exam.exam_management_system.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.exam.exam_management_system.services.ExamService;
import com.exam.exam_management_system.facades.ExamRegistrationFacade;
import com.exam.exam_management_system.models.Exam;
import com.exam.exam_management_system.models.User;
import com.exam.exam_management_system.models.Student;
import com.exam.exam_management_system.models.ExamRegistration;
import com.exam.exam_management_system.models.enums.ExamState;

import jakarta.servlet.http.HttpSession;
import java.util.List;
import java.util.stream.Collectors;
import java.time.LocalDateTime;

@Controller
@RequestMapping("/student")
public class StudentController extends BaseController {

    @Autowired
    private ExamService examService;

    @Autowired
    private ExamRegistrationFacade registrationFacade;

    @GetMapping("/dashboard")
    public String showDashboard(Model model, HttpSession session) {
        addCommonAttributes(model, session);
        User currentUser = getCurrentUser(session);
        if (!(currentUser instanceof Student)) {
            return redirectToLogin();
        }

        List<ExamRegistration> registrations = examService.getStudentRegistrations((Student) currentUser);
        model.addAttribute("registeredExams", registrations);
        return "dashboards/student-dashboard";
    }

    @GetMapping("/schedules")
    public String viewSchedules(Model model, HttpSession session) {
        addCommonAttributes(model, session);
        User currentUser = getCurrentUser(session);
        if (!(currentUser instanceof Student)) {
            return redirectToLogin();
        }

        List<ExamRegistration> registrations = examService.getStudentRegistrations((Student) currentUser);
        model.addAttribute("registrations", registrations);
        return "student/view-schedules";
    }

    @GetMapping("/exam/register")
    public String showExamRegistrationPage(Model model, HttpSession session) {
        addCommonAttributes(model, session);
        List<Exam> availableExams = examService.getAvailableOnlineExams();
        model.addAttribute("availableExams", availableExams);
        model.addAttribute("currentStudent", (Student) getCurrentUser(session));
        return "student/exam-registration";
    }

    @GetMapping("/exam-kiosk")
    public String showExamKiosk(Model model, HttpSession session) {
        addCommonAttributes(model, session);
        Student student = (Student) getCurrentUser(session);
        if (student == null) {
            return redirectToLogin();
        }

        List<ExamRegistration> registrations = examService.getStudentRegistrations(student);
        LocalDateTime now = LocalDateTime.now();

        List<Exam> examsToTake = registrations.stream()
            .map(ExamRegistration::getExam)
            .filter(exam -> exam.getState() == ExamState.ACTIVE && 
                          exam.getExamDate() != null && 
                          !now.isBefore(exam.getExamDate()))
            .collect(Collectors.toList());

        model.addAttribute("examsToTake", examsToTake);
        return "student/exam-kiosk";
    }

    @PostMapping("/exam/register")
    public String registerForExam(@RequestParam Long examId, HttpSession session, RedirectAttributes redirectAttributes) {
        try {
            User currentUser = getCurrentUser(session);
            if (currentUser == null) {
                redirectAttributes.addFlashAttribute("error", "You must be logged in to register for an exam.");
                return "redirect:/login";
            }
            
            if (!(currentUser instanceof Student)) {
                redirectAttributes.addFlashAttribute("error", "Only students can register for exams.");
                return "redirect:/student/exam/register";
            }
            
            registrationFacade.registerStudentForExam((Student) currentUser, examId);
            redirectAttributes.addFlashAttribute("success", "Successfully registered for the exam. Check schedules for details.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/student/exam/register";
    }
} 