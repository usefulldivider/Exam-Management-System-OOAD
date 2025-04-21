// Design Pattern: Builder - Builder class for creating Exam objects
// Design Pattern: State - Uses ExamState for state management

package com.exam.exam_management_system.models;

import com.exam.exam_management_system.models.enums.ExamState;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// Design Pattern: Builder - Builder class
public class ExamBuilder {
    // Architecture Pattern: MVC - Model attributes
    private String subject;
    private LocalDateTime examDate;
    private int duration;
    private int totalMarks;
    private int totalQuestions;
    private User teacher;
    private Teacher creator;
    private boolean showResults = false;
    private boolean allowReview = false;
    private boolean isOnline = false;
    
    // Design Pattern: State - State attribute
    private ExamState state = ExamState.DRAFT;
    
    // Architecture Pattern: MVC - Model attributes
    private List<Question> questions = new ArrayList<>();

    // Design Pattern: Builder - Builder methods
    public ExamBuilder setSubject(String subject) {
        this.subject = subject;
        return this;
    }
    public ExamBuilder setExamDate(LocalDateTime examDate) {
        this.examDate = examDate;
        return this;
    }
    public ExamBuilder setDuration(int duration) {
        this.duration = duration;
        return this;
    }
    public ExamBuilder setTotalMarks(int totalMarks) {
        this.totalMarks = totalMarks;
        return this;
    }
    public ExamBuilder setTotalQuestions(int totalQuestions) {
        this.totalQuestions = totalQuestions;
        return this;
    }
    public ExamBuilder setTeacher(User teacher) {
        this.teacher = teacher;
        return this;
    }
    public ExamBuilder setCreator(Teacher creator) {
        this.creator = creator;
        return this;
    }
    public ExamBuilder setShowResults(boolean showResults) {
        this.showResults = showResults;
        return this;
    }
    public ExamBuilder setAllowReview(boolean allowReview) {
        this.allowReview = allowReview;
        return this;
    }
    public ExamBuilder setOnline(boolean isOnline) {
        this.isOnline = isOnline;
        return this;
    }
    
    // Design Pattern: State - State setter
    public ExamBuilder setState(ExamState state) {
        this.state = state;
        return this;
    }
    
    // Design Pattern: Builder - Builder methods
    public ExamBuilder addQuestion(Question question) {
        this.questions.add(question);
        return this;
    }
    public ExamBuilder setQuestions(List<Question> questions) {
        this.questions = questions;
        return this;
    }
    
    // Design Pattern: Builder - Build method
    public Exam build() {
        Exam exam = new Exam();
        exam.setSubject(subject);
        exam.setExamDate(examDate);
        exam.setDuration(duration);
        exam.setTotalMarks(totalMarks);
        exam.setTotalQuestions(totalQuestions);
        exam.setTeacher(teacher);
        exam.setCreator(creator);
        exam.setShowResults(showResults);
        exam.setAllowReview(allowReview);
        exam.setOnline(isOnline);
        exam.setState(state);
        exam.setQuestions(questions);
        return exam;
    }
} 