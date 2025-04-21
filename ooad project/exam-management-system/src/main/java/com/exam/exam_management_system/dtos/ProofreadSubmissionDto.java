package com.exam.exam_management_system.dtos;

import com.exam.exam_management_system.models.Question;
import java.util.List;
import java.util.ArrayList;

public class ProofreadSubmissionDto {

    private Long examId;
    private List<Question> questions = new ArrayList<>();

    // Getters and Setters
    public Long getExamId() {
        return examId;
    }

    public void setExamId(Long examId) {
        this.examId = examId;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }
}