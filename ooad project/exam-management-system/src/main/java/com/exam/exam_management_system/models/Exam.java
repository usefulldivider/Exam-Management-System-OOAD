// Architecture Pattern: MVC - Model
// Design Pattern: State - Uses state pattern for exam lifecycle management
// Design Pattern: Template Method - Uses state handlers for state transitions

package com.exam.exam_management_system.models;

import com.exam.exam_management_system.models.enums.ExamState;
import com.exam.exam_management_system.models.states.*;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

// Architecture Pattern: MVC - Model
@Entity
@Table(name = "exams")
public class Exam {
    // Architecture Pattern: MVC - Model attributes
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Architecture Pattern: MVC - Model attributes
    @Column(nullable = false)
    private String subject;

    // Architecture Pattern: MVC - Model attributes
    @Column(nullable = true)
    private LocalDateTime examDate;

    // Architecture Pattern: MVC - Model attributes
    @Column(nullable = false)
    private int duration; // in minutes

    // Architecture Pattern: MVC - Model attributes
    @Column(nullable = false)
    private int totalMarks;

    // Architecture Pattern: MVC - Model attributes
    @Column(nullable = false)
    private int totalQuestions;

    // Architecture Pattern: MVC - Model relationships
    @ManyToOne
    @JoinColumn(name = "teacher_id", nullable = false)
    private User teacher;

    // Architecture Pattern: MVC - Model relationships
    @ManyToOne
    @JoinColumn(name = "creator_id")
    private Teacher creator;

    // Architecture Pattern: MVC - Model relationships
    @ManyToOne
    private User proofreader;

    // Architecture Pattern: MVC - Model relationships
    @ManyToOne
    private User publishedBy;

    // Architecture Pattern: MVC - Model relationships
    @ManyToOne
    @JoinColumn(name = "controller_id")
    private ControllerOfExamination controller;

    // Architecture Pattern: MVC - Model relationships
    @OneToMany(mappedBy = "exam", cascade = CascadeType.ALL)
    private List<Question> questions = new ArrayList<>();

    // Architecture Pattern: MVC - Model relationships
    @OneToMany(mappedBy = "exam", cascade = CascadeType.ALL)
    private List<ExamResult> results = new ArrayList<>();

    // Architecture Pattern: MVC - Model relationships
    @OneToMany(mappedBy = "exam", cascade = CascadeType.ALL)
    private List<ExamRegistration> registrations = new ArrayList<>();

    // Architecture Pattern: MVC - Model relationships
    @OneToMany(mappedBy = "exam", cascade = CascadeType.ALL)
    private List<ExamSchedule> schedules = new ArrayList<>();

    // Architecture Pattern: MVC - Model attributes
    @Column(nullable = false)
    private boolean showResults = false;

    // Architecture Pattern: MVC - Model attributes
    @Column(nullable = false)
    private boolean allowReview = false;

    // Architecture Pattern: MVC - Model attributes
    @Column(nullable = false)
    private boolean isOnline = false;

    // Design Pattern: State - State attribute
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ExamState state = ExamState.DRAFT;

    // Design Pattern: State - State handler
    @Transient
    private ExamStateHandler stateHandler;

    // Architecture Pattern: MVC - Model constructor
    public Exam() {
        this.state = ExamState.DRAFT;
        this.stateHandler = new DraftState();
        this.questions = new ArrayList<>();
        this.results = new ArrayList<>();
    }

    // Design Pattern: State - Initialize state handler
    @PostLoad
    public void initStateHandler() {
        setState(this.state);
        if (this.questions == null) {
            this.questions = new ArrayList<>();
        }
        if (this.results == null) {
            this.results = new ArrayList<>();
        }
        if (this.registrations == null) {
            this.registrations = new ArrayList<>();
        }
        if (this.schedules == null) {
            this.schedules = new ArrayList<>();
        }
    }

    // Architecture Pattern: MVC - Model getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public LocalDateTime getExamDate() {
        return examDate;
    }

    public void setExamDate(LocalDateTime examDate) {
        this.examDate = examDate;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getTotalMarks() {
        return totalMarks;
    }

    public void setTotalMarks(int totalMarks) {
        this.totalMarks = totalMarks;
    }

    public int getTotalQuestions() {
        return totalQuestions;
    }

    public void setTotalQuestions(int totalQuestions) {
        this.totalQuestions = totalQuestions;
    }

    public User getTeacher() {
        return teacher;
    }

    public void setTeacher(User teacher) {
        this.teacher = teacher;
    }

    public Teacher getCreator() {
        return creator;
    }

    public void setCreator(Teacher creator) {
        this.creator = creator;
    }

    public User getProofreader() {
        return proofreader;
    }

    public void setProofreader(User proofreader) {
        this.proofreader = proofreader;
    }

    public User getPublishedBy() {
        return publishedBy;
    }

    public void setPublishedBy(User publishedBy) {
        this.publishedBy = publishedBy;
    }

    public ControllerOfExamination getController() {
        return controller;
    }

    public void setController(ControllerOfExamination controller) {
        this.controller = controller;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public List<ExamResult> getResults() {
        return results;
    }

    public void setResults(List<ExamResult> results) {
        this.results = results;
    }

    public List<ExamRegistration> getRegistrations() {
        return registrations;
    }

    public void setRegistrations(List<ExamRegistration> registrations) {
        this.registrations = registrations;
    }

    public List<ExamSchedule> getSchedules() {
        return schedules;
    }

    public void setSchedules(List<ExamSchedule> schedules) {
        this.schedules = schedules;
    }

    public boolean isShowResults() {
        return showResults;
    }

    public void setShowResults(boolean showResults) {
        this.showResults = showResults;
    }

    public boolean isAllowReview() {
        return allowReview;
    }

    public void setAllowReview(boolean allowReview) {
        this.allowReview = allowReview;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }

    // Design Pattern: State - State getter
    public ExamState getState() {
        return state;
    }

    // Design Pattern: State - State setter with state handler initialization
    public void setState(ExamState state) {
        this.state = state;
        switch (state) {
            case DRAFT -> stateHandler = new DraftState();
            case PROOFREAD -> stateHandler = new ProofreadState();
            case STORED_IN_COE -> stateHandler = new StoredInCoeState();
            case ACTIVE -> stateHandler = new ActiveState();
            case COMPLETED -> stateHandler = new CompletedState();
            case EVALUATED -> stateHandler = new EvaluatedState();
            case RESULTS_PUBLISHED -> stateHandler = new ResultsPublishedState();
            default -> stateHandler = new DraftState();
        }
    }

    // Design Pattern: State - State transition method
    public void nextState() {
        if (stateHandler == null) {
            initStateHandler();
        }
        stateHandler.next(this);
    }

    // Architecture Pattern: MVC - Model derived property
    @Transient
    public List<User> getRegisteredStudents() {
        if (registrations == null) return new ArrayList<>();
        return registrations.stream()
                .map(ExamRegistration::getStudent)
                .collect(Collectors.toList());
    }
}
