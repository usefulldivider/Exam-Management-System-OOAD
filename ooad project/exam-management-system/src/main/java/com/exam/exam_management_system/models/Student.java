package com.exam.exam_management_system.models;

import jakarta.persistence.*;
import java.util.Set;

import com.exam.exam_management_system.models.enums.UserRole;

@Entity

public class Student extends User {

    @Column(unique = true)
    private String studentId;

    @ManyToMany
    private Set<Exam> registeredExams;

    public Student() {
        // Default constructor
    }

    public Student(String username, String password, String name, String email, String studentId) {
        super(username, password, name, email, UserRole.STUDENT);
        this.studentId = studentId;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public Set<Exam> getRegisteredExams() {
        return registeredExams;
    }

    public void setRegisteredExams(Set<Exam> registeredExams) {
        this.registeredExams = registeredExams;
    }
}
