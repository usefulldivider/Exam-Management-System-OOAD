package com.exam.exam_management_system.models;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "exam_schedules")
public class ExamSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(optional = false)
    @JoinColumn(name = "exam_id")
    private Exam exam;

    @Column(nullable = false)
    private LocalDate examDate;
    
    @Column(nullable = false)
    private LocalDateTime startTime;
    
    @Column(nullable = true)
    private LocalDateTime endTime;
    
    @Column(length = 100)
    private String venue;
    
    @Column(length = 1000)
    private String seatArrangement;
    
    @OneToOne
    @JoinColumn(name = "registration_id")
    private ExamRegistration registration;
    
    @ManyToOne
    @JoinColumn(name = "coordinator_id")
    private ExamCoordinator coordinator;
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Exam getExam() {
        return exam;
    }
    
    public void setExam(Exam exam) {
        this.exam = exam;
    }
    
    public LocalDate getExamDate() {
        return examDate;
    }
    
    public void setExamDate(LocalDate examDate) {
        this.examDate = examDate;
    }
    
    public LocalDateTime getStartTime() {
        return startTime;
    }
    
    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }
    
    public LocalDateTime getEndTime() {
        return endTime;
    }
    
    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }
    

    
    public ExamRegistration getRegistration() {
        return registration;
    }
    
    public void setRegistration(ExamRegistration registration) {
        this.registration = registration;
    }
    
    public ExamCoordinator getCoordinator() {
        return coordinator;
    }
    
    public void setCoordinator(ExamCoordinator coordinator) {
        this.coordinator = coordinator;
    }
}