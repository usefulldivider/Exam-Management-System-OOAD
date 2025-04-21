package com.exam.exam_management_system.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "exam_registrations")
public class ExamRegistration {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(optional = false)
    @JoinColumn(name = "student_id")
    private Student student;
    
    @ManyToOne(optional = false)
    @JoinColumn(name = "exam_id")
    private Exam exam;
    
    @ManyToOne
    @JoinColumn(name = "coordinator_id")
    private ExamCoordinator coordinator;
    
    @Column(name = "registration_date")
    private LocalDateTime registrationDate;
    
    @OneToOne(mappedBy = "registration")
    private ExamSchedule schedule;
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Student getStudent() {
        return student;
    }
    
    public void setStudent(Student student) {
        this.student = student;
    }
    
    public Exam getExam() {
        return exam;
    }
    
    public void setExam(Exam exam) {
        this.exam = exam;
    }
    
    public ExamCoordinator getCoordinator() {
        return coordinator;
    }
    
    public void setCoordinator(ExamCoordinator coordinator) {
        this.coordinator = coordinator;
    }
    
    public LocalDateTime getRegistrationDate() {
        return registrationDate;
    }
    
    public void setRegistrationDate(LocalDateTime registrationDate) {
        this.registrationDate = registrationDate;
    }
    
    public ExamSchedule getSchedule() {
        return schedule;
    }
    
    public void setSchedule(ExamSchedule schedule) {
        this.schedule = schedule;
    }
}