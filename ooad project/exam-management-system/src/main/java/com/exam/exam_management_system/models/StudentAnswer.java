package com.exam.exam_management_system.models;
import com.exam.exam_management_system.models.enums.*;
import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
@Table(name = "student_answers")
public class StudentAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @Enumerated(EnumType.STRING)
    @Column(name = "selected_option", nullable = false)
    private MCQOption selectedOption;

    @Column(name = "submission_time", nullable = false)
    private LocalDateTime submissionTime;

    @Column(name = "marks")
    private Integer marks;

    // Default constructor
    public StudentAnswer() {
    }

    // Parameterized constructor
    public StudentAnswer(Question question, Student student, MCQOption selectedOption) {
        this.question = question;
        this.student = student;
        this.selectedOption = selectedOption;
        this.submissionTime = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public MCQOption getSelectedOption() {
        return selectedOption;
    }

    public void setSelectedOption(MCQOption selectedOption) {
        this.selectedOption = selectedOption;
    }

    public LocalDateTime getSubmissionTime() {
        return submissionTime;
    }

    public void setSubmissionTime(LocalDateTime submissionTime) {
        this.submissionTime = submissionTime;
    }

    public Integer getMarks() {
        return marks;
    }

    public void setMarks(Integer marks) {
        this.marks = marks;
    }
}