package com.exam.exam_management_system.controllers;

import com.exam.exam_management_system.models.*;
import com.exam.exam_management_system.models.enums.MCQOption;
import com.exam.exam_management_system.services.ExamService;
import com.exam.exam_management_system.dtos.ProofreadSubmissionDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
// import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
// import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/teacher")
public class TeacherController extends BaseController {

    @Autowired
    private ExamService examService;

    @GetMapping("/exam-papers/create")
    public String showExamDetailsForm(Model model, HttpSession session) {
        addCommonAttributes(model, session);
        return "teacher/exam-details";
    }

    @PostMapping("/exams/create/details")
    public String createExamDetails(
            @RequestParam String subject,
            @RequestParam int duration,
            @RequestParam int totalQuestions,
            Model model,
            HttpSession session) {
        
        // Get current teacher
        User currentUser = getCurrentUser(session);
        if (!(currentUser instanceof Teacher)) {
            return redirectToLogin();
        }
        
        // Create initial exam with basic details provided by teacher
        Exam exam = new ExamBuilder()
            .setSubject(subject)
            .setDuration(duration)
            .setTotalQuestions(totalQuestions)
            .setTotalMarks(totalQuestions) // Each question is 1 mark
            .setState(com.exam.exam_management_system.models.enums.ExamState.DRAFT)
            .setTeacher(currentUser)
            .setCreator((Teacher) currentUser)
            .build();
        // Save the exam to get an ID
        exam = examService.createExam(exam, currentUser);
        
        // Add to model for the next step
        model.addAttribute("exam", exam);
        model.addAttribute("currentQuestionIndex", 0);
        
        return "teacher/exam-questions";
    }

    @PostMapping("/exams/create/questions")
    public String addQuestion(
            @RequestParam Long examId,
            @RequestParam int questionIndex,
            @RequestParam String questionText,
            @RequestParam String optionA,
            @RequestParam String optionB,
            @RequestParam String optionC,
            @RequestParam String optionD,
            @RequestParam String correctOption,
            Model model,
            HttpSession session) {
        
        User currentUser = getCurrentUser(session);
        if (!(currentUser instanceof Teacher)) {
            return redirectToLogin();
        }
        
        // Build the question object (without setting the exam initially)
        Question question = new QuestionBuilder()
            .setQuestionText(questionText)
            .setOptionA(optionA)
            .setOptionB(optionB)
            .setOptionC(optionC)
            .setOptionD(optionD)
            .setCorrectAnswer(MCQOption.valueOf(correctOption))
            .setMarks(1)
            .build();
        
        try {
            // Use the new service method to add and save the question
            examService.addQuestionToExam(examId, question, currentUser);
        } catch (Exception e) {
            // Handle exceptions (e.g., exam not found, wrong state, unauthorized)
             model.addAttribute("error", "Failed to add question: " + e.getMessage());
             // Need to refetch exam details for the view if returning to the same page
             Exam exam = examService.getExamById(examId);
             model.addAttribute("exam", exam);
             model.addAttribute("currentQuestionIndex", questionIndex); 
             return "teacher/exam-questions";
        }

        // Fetch the exam again AFTER adding the question to get the updated state/count if needed
        Exam exam = examService.getExamById(examId);

        // If this was the last question, redirect to dashboard
        // Check against the actual number of questions added, not just the index
        if (exam.getQuestions().size() >= exam.getTotalQuestions()) {
            return "redirect:/teacher/dashboard";
        }
        
        // Otherwise, show the next question form
        model.addAttribute("exam", exam);
        // Pass the size of the list as the next index
        model.addAttribute("currentQuestionIndex", exam.getQuestions().size());
        
        return "teacher/exam-questions";
    }

    @GetMapping("/dashboard")
    public String showDashboard(Model model, HttpSession session) {
        addCommonAttributes(model, session);
        User currentUser = getCurrentUser(session);
        if (!(currentUser instanceof Teacher)) {
            return redirectToLogin();
        }
        
        // Fetch draft exams created by this teacher
        List<Exam> myDraftExams = examService.getDraftExams(currentUser);
        model.addAttribute("myDraftExams", myDraftExams);
        
        // Fetch count of exams pending proofread by this teacher (for badge/indicator)
        // We can add a count method to the service later if needed for performance
        List<Exam> pendingProofread = examService.getExamsPendingProofread(currentUser);
        model.addAttribute("pendingProofreadCount", pendingProofread.size());
        
        return "dashboards/teacher-dashboard";
    }

    /**
     * Handles the request from the exam creator to submit their DRAFT exam for proofreading.
     */
    @PostMapping("/exams/{examId}/submit-proofread")
    public String submitExamForProofreading(@PathVariable Long examId, HttpSession session, RedirectAttributes redirectAttributes) {
        User currentUser = getCurrentUser(session);
        if (!(currentUser instanceof Teacher)) {
            return redirectToLogin();
        }

        try {
            Exam exam = examService.getExamById(examId);

            // Check 1: Exam must be in DRAFT state
            if (exam.getState() != com.exam.exam_management_system.models.enums.ExamState.DRAFT) {
                redirectAttributes.addFlashAttribute("error", "Exam is not in DRAFT state and cannot be submitted for proofread.");
                return "redirect:/teacher/dashboard"; // Or back to exam details page
            }

            // Check 2: Only the creator can submit it
            if (exam.getCreator() == null || !exam.getCreator().getId().equals(currentUser.getId())) {
                 redirectAttributes.addFlashAttribute("error", "Only the creator can submit this exam for proofread.");
                 return "redirect:/teacher/dashboard"; 
            }

            // Proceed with submission
            examService.submitExamForProofreading(examId); // Service method needs to be created
            redirectAttributes.addFlashAttribute("success", "Exam submitted successfully for proofreading.");

        } catch (RuntimeException e) {
            // Handle exceptions like exam not found
            redirectAttributes.addFlashAttribute("error", "Error submitting exam: " + e.getMessage());
        }

        return "redirect:/teacher/dashboard";
    }

    /**
     * Displays a list of exams pending proofread by other teachers.
     */
    @GetMapping("/exams/pending-proofread")
    public String showPendingProofreadExams(Model model, HttpSession session) {
        User currentUser = getCurrentUser(session);
        if (!(currentUser instanceof Teacher)) {
            return redirectToLogin(); // Only teachers can proofread
        }
        
        addCommonAttributes(model, session); // Add user info etc.
        
        List<Exam> pendingExams = examService.getExamsPendingProofread(currentUser);
        model.addAttribute("pendingExams", pendingExams);
        
        return "teacher/pending-proofread"; // Path to the new Thymeleaf template
    }

    /**
     * Displays the form for a teacher to proofread an exam created by another teacher.
     * Pre-populates the form with existing questions and answers.
     */
    @GetMapping("/exams/{examId}/proofread")
    public String showProofreadExamForm(@PathVariable Long examId, Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        User currentUser = getCurrentUser(session);
        if (!(currentUser instanceof Teacher)) {
            return redirectToLogin();
        }

        try {
            Exam exam = examService.getExamById(examId);

            // Check 1: Exam must be in PROOFREAD state
            if (exam.getState() != com.exam.exam_management_system.models.enums.ExamState.PROOFREAD) {
                redirectAttributes.addFlashAttribute("error", "This exam is not currently awaiting proofread.");
                return "redirect:/teacher/dashboard";
            }

            // Check 2: Cannot proofread own exam
            if (exam.getCreator() != null && exam.getCreator().getId().equals(currentUser.getId())) {
                 redirectAttributes.addFlashAttribute("error", "You cannot proofread an exam you created.");
                 return "redirect:/teacher/exams/pending-proofread"; 
            }

            addCommonAttributes(model, session);
            model.addAttribute("exam", exam); // Pass the full exam object with questions
            // Ensure questions are loaded if lazy-loaded
            // exam.getQuestions().size(); // Example way to trigger load if needed
            
            return "teacher/proofread-exam"; // Path to the new proofread template

        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", "Error loading exam for proofread: " + e.getMessage());
            return "redirect:/teacher/dashboard";
        }
    }

    /**
     * Processes the submitted proofread changes for an exam.
     */
    @PostMapping("/exams/{examId}/process-proofread")
    public String processProofreadExam(@PathVariable Long examId,
                                       @ModelAttribute ProofreadSubmissionDto submissionDto,
                                       HttpSession session,
                                       RedirectAttributes redirectAttributes) {
        User currentUser = getCurrentUser(session);
        if (!(currentUser instanceof Teacher)) {
            return redirectToLogin();
        }

        // Basic validation: Check if examId matches and questions are present
        if (submissionDto == null || submissionDto.getQuestions() == null || submissionDto.getQuestions().isEmpty()) {
             redirectAttributes.addFlashAttribute("error", "Invalid submission data received.");
             return "redirect:/teacher/exams/pending-proofread";
        }
        // Ensure the path variable examId matches the one potentially in the DTO if needed
        // submissionDto.setExamId(examId); 

        try {
            // Fetch the exam to double-check state and creator vs proofreader
            Exam exam = examService.getExamById(examId);
            if (exam.getState() != com.exam.exam_management_system.models.enums.ExamState.PROOFREAD) {
                redirectAttributes.addFlashAttribute("error", "This exam is not awaiting proofread.");
                return "redirect:/teacher/dashboard";
            }
            if (exam.getCreator() != null && exam.getCreator().getId().equals(currentUser.getId())) {
                 redirectAttributes.addFlashAttribute("error", "You cannot proofread an exam you created.");
                 return "redirect:/teacher/exams/pending-proofread"; 
            }
            
            // Call the service method with the list of questions from the DTO
            examService.processProofreadSubmission(examId, currentUser, submissionDto.getQuestions());
            
            redirectAttributes.addFlashAttribute("success", "Exam proofread changes submitted successfully.");
            return "redirect:/teacher/dashboard"; // Redirect to dashboard after successful proofread

        } catch (IllegalArgumentException | IllegalStateException e) {
            // Handle specific validation errors from the service
            redirectAttributes.addFlashAttribute("error", "Error processing proofread: " + e.getMessage());
            // Redirect back to the proofread form to show errors and allow correction
            // Need to add examId back to the path
            return "redirect:/teacher/exams/" + examId + "/proofread"; 
        } catch (RuntimeException e) {
            // Handle other unexpected errors
            redirectAttributes.addFlashAttribute("error", "An unexpected error occurred: " + e.getMessage());
            return "redirect:/teacher/dashboard";
        }
    }
} 