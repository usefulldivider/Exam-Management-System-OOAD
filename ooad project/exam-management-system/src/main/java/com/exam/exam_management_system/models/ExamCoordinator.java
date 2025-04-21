package com.exam.exam_management_system.models;
import com.exam.exam_management_system.models.enums.UserRole;
import com.exam.exam_management_system.models.enums.AccountStatus;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "exam_coordinator")
public class ExamCoordinator extends User {

    @OneToMany(mappedBy = "coordinator")
    private List<ExamRegistration> registrations;

    @OneToMany(mappedBy = "coordinator")
    private List<ExamSchedule> schedules;

    @Column
    private LocalDateTime lastScheduleUpdate;

    public ExamCoordinator() {
        super();
        setRole(UserRole.EXAM_COORDINATOR);
        setStatus(AccountStatus.APPROVED);
    }

    public ExamCoordinator(String username, String password, String name, String email) {
        super(username, password, name, email, UserRole.EXAM_COORDINATOR);
        setStatus(AccountStatus.APPROVED);
    }

    public ExamCoordinator(String username, String password, String name, String email, String additional) {
        this(username, password, name, email);
    }

    // Getters and Setters
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

    public LocalDateTime getLastScheduleUpdate() {
        return lastScheduleUpdate;
    }

    public void setLastScheduleUpdate(LocalDateTime lastScheduleUpdate) {
        this.lastScheduleUpdate = lastScheduleUpdate;
    }

    public ExamSchedule createExamSchedule(Exam exam, LocalDate examDate, LocalDateTime startTime) {
        ExamSchedule schedule = new ExamSchedule();
        schedule.setExam(exam);
        schedule.setExamDate(examDate);
        schedule.setStartTime(startTime);
        schedule.setCoordinator(this);
        this.lastScheduleUpdate = LocalDateTime.now();
        return schedule;
    }

    public void updateSchedule(ExamSchedule schedule, LocalDate newDate, LocalDateTime newStartTime) {
        schedule.setExamDate(newDate);
        schedule.setStartTime(newStartTime);
        this.lastScheduleUpdate = LocalDateTime.now();
    }
}

