package com.exam.exam_management_system.models.states;
import com.exam.exam_management_system.models.ExamStateHandler;
import com.exam.exam_management_system.models.enums.ExamState;
import com.exam.exam_management_system.models.Exam;

public class ScheduledState implements ExamStateHandler {
    @Override
    public void next(Exam exam) {
        exam.setState(ExamState.ACTIVE);
    }
} 