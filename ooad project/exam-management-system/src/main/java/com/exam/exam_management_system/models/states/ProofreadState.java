package com.exam.exam_management_system.models.states;
import com.exam.exam_management_system.models.ExamStateHandler;
import com.exam.exam_management_system.models.enums.ExamState;
import com.exam.exam_management_system.models.Exam;

public class ProofreadState implements ExamStateHandler {
    @Override
    public void next(Exam exam) {
        exam.setState(ExamState.STORED_IN_COE);
    }
}