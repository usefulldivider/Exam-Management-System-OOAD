package com.exam.exam_management_system.commands;

import com.exam.exam_management_system.models.Exam;
import com.exam.exam_management_system.services.ExamService;
import com.exam.exam_management_system.models.enums.ExamState;

public class SetPaperOfflineCommand implements PaperCommand {
    private final Exam exam;
    private final ExamService examService;

    public SetPaperOfflineCommand(Exam exam, ExamService examService) {
        this.exam = exam;
        this.examService = examService;
    }

    @Override
    public void execute() {
        exam.setOnline(false);
        exam.setState(ExamState.STORED_IN_COE);
        examService.saveExam(exam);
    }
} 