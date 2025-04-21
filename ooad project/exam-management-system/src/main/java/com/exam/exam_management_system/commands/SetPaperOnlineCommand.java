// Design Pattern: Command - Concrete command implementation for setting paper online
// Design Pattern: State - Uses ExamState for state management

package com.exam.exam_management_system.commands;

import com.exam.exam_management_system.models.Exam;
import com.exam.exam_management_system.services.ExamService;
import com.exam.exam_management_system.models.enums.ExamState;

// Design Pattern: Command - Concrete command implementation
public class SetPaperOnlineCommand implements PaperCommand {
    // Design Pattern: Command - Command receiver (Exam)
    private final Exam exam;
    
    // Architecture Pattern: Service Layer Pattern - Service injection
    private final ExamService examService;

    // Design Pattern: Command - Command constructor
    public SetPaperOnlineCommand(Exam exam, ExamService examService) {
        this.exam = exam;
        this.examService = examService;
    }

    // Design Pattern: Command - Execute method implementation
    @Override
    public void execute() {
        exam.setOnline(true);
        // Design Pattern: State - State transition
        exam.setState(ExamState.ACTIVE);
        examService.saveExam(exam);
    }
} 