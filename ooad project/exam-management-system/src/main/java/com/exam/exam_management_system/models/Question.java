// Architecture Pattern: MVC - Model
// Design Pattern: Entity - JPA entity representing a question in the system

package com.exam.exam_management_system.models;

import com.exam.exam_management_system.models.enums.*;

import jakarta.persistence.*;
import java.util.List;
import java.util.ArrayList;

// Architecture Pattern: MVC - Model
@Entity
@Table(name = "questions")
public class Question {
    
    // Architecture Pattern: MVC - Model attributes
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;        

    // Architecture Pattern: MVC - Model attributes
    @Column(nullable = false, length = 1000)
    private String questionText;

    // Architecture Pattern: MVC - Model attributes
    @Column(nullable = false)
    private String optionA;

    // Architecture Pattern: MVC - Model attributes
    @Column(nullable = false)
    private String optionB;

    // Architecture Pattern: MVC - Model attributes
    @Column(nullable = false)
    private String optionC;

    // Architecture Pattern: MVC - Model attributes
    @Column(nullable = false)
    private String optionD;

    // Architecture Pattern: MVC - Model attributes
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MCQOption correctAnswer;

    // Architecture Pattern: MVC - Model attributes
    @Column(nullable = false)
    private Integer marks;

    // Architecture Pattern: MVC - Model relationships
    @ManyToOne
    @JoinColumn(name = "exam_id", nullable = false)
    private Exam exam;

    // Architecture Pattern: MVC - Model relationships
    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL)
    private List<StudentAnswer> studentAnswers = new ArrayList<>();

    // Architecture Pattern: MVC - Model getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public String getOptionA() {
        return optionA;
    }

    public void setOptionA(String optionA) {
        this.optionA = optionA;
    }

    public String getOptionB() {
        return optionB;
    }

    public void setOptionB(String optionB) {
        this.optionB = optionB;
    }

    public String getOptionC() {
        return optionC;
    }

    public void setOptionC(String optionC) {
        this.optionC = optionC;
    }

    public String getOptionD() {
        return optionD;
    }

    public void setOptionD(String optionD) {
        this.optionD = optionD;
    }

    public MCQOption getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(MCQOption correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public Integer getMarks() {
        return marks;
    }

    public void setMarks(Integer marks) {
        this.marks = marks;
    }

    public Exam getExam() {
        return exam;
    }

    public void setExam(Exam exam) {
        this.exam = exam;
    }

    public List<StudentAnswer> getStudentAnswers() {
        return studentAnswers;
    }

    public void setStudentAnswers(List<StudentAnswer> studentAnswers) {
        this.studentAnswers = studentAnswers;
    }
}
