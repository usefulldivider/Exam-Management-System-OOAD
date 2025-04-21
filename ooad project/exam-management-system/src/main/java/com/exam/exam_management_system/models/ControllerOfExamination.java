package com.exam.exam_management_system.models;
import com.exam.exam_management_system.models.enums.UserRole;
import com.exam.exam_management_system.models.enums.AccountStatus;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "controller_of_examination")
public class ControllerOfExamination extends User {

    @OneToMany(mappedBy = "controller")
    private List<Exam> managedExams;

    @Column
    private LocalDateTime lastExamDistribution;

    @Column
    private LocalDateTime lastResultsPublication;

    public ControllerOfExamination() {
        super();
        setRole(UserRole.CONTROLLER_OF_EXAMINATION);
        setStatus(AccountStatus.APPROVED);
    }

    public ControllerOfExamination(String username, String password, String name, String email) {
        super(username, password, name, email, UserRole.CONTROLLER_OF_EXAMINATION);
        setStatus(AccountStatus.APPROVED);
    }

    public ControllerOfExamination(String username, String password, String name, String email, String additional) {
        this(username, password, name, email);
    }

}
