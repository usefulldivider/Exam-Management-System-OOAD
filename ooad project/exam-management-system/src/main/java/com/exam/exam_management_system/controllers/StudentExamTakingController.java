package com.exam.exam_management_system.controllers;

import com.exam.exam_management_system.exam.MCQExamSession;
import com.exam.exam_management_system.models.*;
import com.exam.exam_management_system.models.enums.MCQOption;
import com.exam.exam_management_system.services.ExamService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/student/exam-taking")
public class StudentExamTakingController extends BaseController {

    @Autowired
    private ExamService examService;

    @GetMapping("/start/{examId}")
    public String startExamRedirect(@PathVariable Long examId, Model model, HttpSession session) {
        return startExam(examId, model, session);
    }

    @GetMapping("/{examId}")
    public String startExam(@PathVariable Long examId, Model model, HttpSession session) {
        Student student = (Student) getCurrentUser(session);
        Exam exam = examService.getExamById(examId);

        // Check if student is registered for this exam
        if (!examService.isStudentRegistered(exam, student)) {
            return "redirect:/student/dashboard?error=not_registered";
        }

        // Initialize MCQExamSession
        MCQExamSession examSession = new MCQExamSession(exam, student, examService);
        
        // Check if exam is within the allowed time window
        if (!examSession.canStartExam()) {
            return "redirect:/student/dashboard?error=invalid_time";
        }

        model.addAttribute("exam", exam);
        model.addAttribute("student", student);
        model.addAttribute("currentQuestionIndex", 0);
        return "student/exam-taking";
    }

    @PostMapping("/{examId}/submit")
    public String submitExam(
            @PathVariable Long examId,
            @RequestParam Map<String, String> answers,
            HttpSession session) {
        
        Student student = (Student) getCurrentUser(session);
        Exam exam = examService.getExamById(examId);
        
        List<StudentAnswer> studentAnswers = new ArrayList<>();
        
        // Process each answer
        for (Map.Entry<String, String> entry : answers.entrySet()) {
            if (entry.getKey().startsWith("answer_")) {
                Long questionId = Long.parseLong(entry.getKey().substring(7));
                Question question = examService.getQuestionById(questionId);
                
                StudentAnswer answer = new StudentAnswer();
                answer.setQuestion(question);
                answer.setStudent(student);
                answer.setSelectedOption(MCQOption.valueOf(entry.getValue()));
                answer.setSubmissionTime(LocalDateTime.now());
                studentAnswers.add(answer);
                examService.saveStudentAnswer(answer);
            }
        }
        
        // Create and evaluate exam result
        ExamResult result = evaluateMCQAnswers(exam, student, studentAnswers);
        examService.saveExamResult(result);
        
        // Update exam state to COMPLETED
        examService.completeExam(exam);
        
        // Redirect to results page
        return "redirect:/student/exam-taking/" + examId + "/results";
    }

    @GetMapping("/{examId}/results")
    public String showResults(@PathVariable Long examId, Model model, HttpSession session) {
        Student student = (Student) getCurrentUser(session);
        Exam exam = examService.getExamById(examId);
        
        // Get the latest result for this exam and student
        List<StudentAnswer> answers = examService.getStudentAnswers(examId, student);
        ExamResult result = examService.getExamResultByExamAndStudent(exam, student);
        
        if (result == null) {
            // If no result exists yet, evaluate the exam now
            result = evaluateMCQAnswers(exam, student, answers);
            examService.saveExamResult(result);
        }

        model.addAttribute("exam", exam);
        model.addAttribute("result", result);
        model.addAttribute("answers", answers);
        return "student/exam-results";
    }

    /**
     * Evaluates MCQ answers and returns the exam result
     */
    private ExamResult evaluateMCQAnswers(Exam exam, Student student, List<StudentAnswer> answers) {
        int totalMarks = 0;
        
        // Calculate total marks by comparing selected answers with correct answers
        for (StudentAnswer answer : answers) {
            if (answer.getSelectedOption() == answer.getQuestion().getCorrectAnswer()) {
                totalMarks += answer.getQuestion().getMarks();
            }
        }
        
        // Create and populate the exam result
        ExamResult result = new ExamResult();
        result.setExam(exam);
        result.setStudent(student);
        result.setMarksObtained(totalMarks);
        result.setTotalMarks(exam.getTotalMarks());
        result.setSubmissionTime(LocalDateTime.now());
        
        // Determine pass/fail status (50% passing threshold)
        double passingThreshold = exam.getTotalMarks() * 0.5;
        result.setStatus(totalMarks >= passingThreshold ? "PASSED" : "FAILED");
        
        return result;
    }
} 