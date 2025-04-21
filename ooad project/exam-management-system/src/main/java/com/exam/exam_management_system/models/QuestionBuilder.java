// Design Pattern: Builder - Builder class for creating Question objects

package com.exam.exam_management_system.models;

import com.exam.exam_management_system.models.enums.MCQOption;

// Design Pattern: Builder - Builder class
public class QuestionBuilder {
    // Architecture Pattern: MVC - Model attributes
    private String questionText;
    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;
    private MCQOption correctAnswer;
    private Integer marks;
    private Exam exam;

    // Design Pattern: Builder - Builder methods
    public QuestionBuilder setQuestionText(String questionText) {
        this.questionText = questionText;
        return this;
    }
    public QuestionBuilder setOptionA(String optionA) {
        this.optionA = optionA;
        return this;
    }
    public QuestionBuilder setOptionB(String optionB) {
        this.optionB = optionB;
        return this;
    }
    public QuestionBuilder setOptionC(String optionC) {
        this.optionC = optionC;
        return this;
    }
    public QuestionBuilder setOptionD(String optionD) {
        this.optionD = optionD;
        return this;
    }
    public QuestionBuilder setCorrectAnswer(MCQOption correctAnswer) {
        this.correctAnswer = correctAnswer;
        return this;
    }
    public QuestionBuilder setMarks(Integer marks) {
        this.marks = marks;
        return this;
    }
    public QuestionBuilder setExam(Exam exam) {
        this.exam = exam;
        return this;
    }
    
    // Design Pattern: Builder - Build method
    public Question build() {
        Question question = new Question();
        question.setQuestionText(questionText);
        question.setOptionA(optionA);
        question.setOptionB(optionB);
        question.setOptionC(optionC);
        question.setOptionD(optionD);
        question.setCorrectAnswer(correctAnswer);
        question.setMarks(marks);
        question.setExam(exam);
        return question;
    }
} 