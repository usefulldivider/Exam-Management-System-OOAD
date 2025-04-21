package com.exam.exam_management_system.evaluation;

import com.exam.exam_management_system.models.Exam;
import com.exam.exam_management_system.models.Student;
import com.exam.exam_management_system.models.StudentAnswer;
import com.exam.exam_management_system.models.ExamResult;

import java.util.List;

/**
 * Interface defining the contract for all evaluation strategies.
 * This follows the Strategy pattern to allow different evaluation methods.
 */
public interface EvaluationStrategy {
    /**
     * Evaluates a student's answers for an exam and returns the result.
     *
     * @param exam The exam being evaluated
     * @param student The student whose answers are being evaluated
     * @param answers List of student's answers to evaluate
     * @return ExamResult containing the evaluation results
     */
    ExamResult evaluate(Exam exam, Student student, List<StudentAnswer> answers);
} 