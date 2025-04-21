package com.exam.exam_management_system.evaluation;

import com.exam.exam_management_system.models.Exam;
import com.exam.exam_management_system.models.Student;
import com.exam.exam_management_system.models.StudentAnswer;
import com.exam.exam_management_system.models.ExamResult;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Concrete implementation of EvaluationStrategy for MCQ evaluation.
 * This strategy automatically evaluates MCQ answers by comparing selected options with correct answers.
 */
public class AutoMCQEvaluationStrategy implements EvaluationStrategy {
    
    @Override
    public ExamResult evaluate(Exam exam, Student student, List<StudentAnswer> answers) {
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