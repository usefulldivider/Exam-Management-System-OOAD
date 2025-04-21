// Design Pattern: State - State handler interface for exam state transitions

package com.exam.exam_management_system.models;

// Design Pattern: State - State handler interface
public interface ExamStateHandler {
    // Design Pattern: State - Method to transition to next state
    void next(Exam exam);
}
