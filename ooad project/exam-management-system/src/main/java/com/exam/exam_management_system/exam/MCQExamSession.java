package com.exam.exam_management_system.exam;

import com.exam.exam_management_system.models.*;
import com.exam.exam_management_system.models.enums.MCQOption;
import com.exam.exam_management_system.services.ExamService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MCQExamSession extends ExamSession {
    private List<Question> questions;
    private int currentQuestionIndex = 0;
    private List<StudentAnswer> answers = new ArrayList<>();

    public MCQExamSession(Exam exam, Student student, ExamService examService) {
        super(exam, student, examService);
    }

    @Override
    protected void initializeExam() {
        questions = examService.getQuestions(exam.getId());
        if (questions == null || questions.isEmpty()) {
            throw new IllegalStateException("No questions found for this exam");
        }
    }

    @Override
    protected void processAnswer() {
        if (currentQuestionIndex >= questions.size()) {
            isCompleted = true;
            return;
        }

        Question currentQuestion = questions.get(currentQuestionIndex);
        // Get the student's answer (would come from the controller)
        MCQOption selectedOption = getStudentAnswerForCurrentQuestion();
        
        StudentAnswer answer = new StudentAnswer();
        answer.setQuestion(currentQuestion);
        answer.setStudent(student);
        answer.setSelectedOption(selectedOption);
        answer.setSubmissionTime(LocalDateTime.now());
        
        answers.add(answer);
        currentQuestionIndex++;
    }

    @Override
    protected void evaluateExam() {
        ExamResult result = evaluateMCQAnswers();
        examService.saveExamResult(result);
    }

    @Override
    protected void saveCurrentAnswer() {
        for (StudentAnswer answer : answers) {
            examService.saveStudentAnswer(answer);
        }
    }

    // Helper method to get student's answer (would be implemented based on your UI)
    private MCQOption getStudentAnswerForCurrentQuestion() {
        // This would be implemented to get the answer from the student's input
        // For now, returning null as a placeholder
        return null;
    }

    /**
     * Evaluates MCQ answers and returns the exam result
     * @return ExamResult containing the evaluation results
     */
    private ExamResult evaluateMCQAnswers() {
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