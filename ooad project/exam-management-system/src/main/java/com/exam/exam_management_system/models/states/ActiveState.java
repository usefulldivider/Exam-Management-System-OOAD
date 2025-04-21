// Design Pattern: State - Concrete state implementation for Active state

package com.exam.exam_management_system.models.states;
import com.exam.exam_management_system.models.ExamStateHandler;
import com.exam.exam_management_system.models.enums.ExamState;
import com.exam.exam_management_system.models.Exam;

// Design Pattern: State - Concrete state implementation
public class ActiveState implements ExamStateHandler {
    // Design Pattern: State - State transition implementation
    @Override
    public void next(Exam exam) {
        exam.setState(ExamState.COMPLETED);
    }
}
