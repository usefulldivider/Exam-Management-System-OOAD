package com.exam.exam_management_system.models.states;
import com.exam.exam_management_system.models.ExamStateHandler;
import com.exam.exam_management_system.models.Exam;

public class ResultsPublishedState implements ExamStateHandler {
    @Override
    public void next(Exam exam) {
        // No next state after results are published
    }
} 