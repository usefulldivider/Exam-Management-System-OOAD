package com.exam.exam_management_system.models;

import jakarta.persistence.*;
import java.util.Set;

import com.exam.exam_management_system.models.enums.UserRole;

@Entity

public class Teacher extends User {

    private String department;

    @OneToMany(mappedBy = "creator")
    private Set<Exam> createdExams;

    @OneToMany(mappedBy = "proofreader")
    private Set<Exam> proofreadExams;

    public Teacher() {
        // Default constructor
    }

    public Teacher(String username, String password, String name, String email, String department) {
        super(username, password, name, email, UserRole.TEACHER);
        this.department = department;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public Set<Exam> getCreatedExams() {
        return createdExams;
    }

    public void setCreatedExams(Set<Exam> createdExams) {
        this.createdExams = createdExams;
    }

    public Set<Exam> getProofreadExams() {
        return proofreadExams;
    }

    public void setProofreadExams(Set<Exam> proofreadExams) {
        this.proofreadExams = proofreadExams;
    }
}
