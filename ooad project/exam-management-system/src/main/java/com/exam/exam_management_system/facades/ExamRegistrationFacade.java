// Design Pattern: Facade - Provides a simplified interface to exam registration operations
// Architecture Pattern: Service Layer Pattern - Orchestrates multiple services

package com.exam.exam_management_system.facades;

import com.exam.exam_management_system.models.*;
import com.exam.exam_management_system.services.ExamService;
import com.exam.exam_management_system.services.ExamRegistrationService;
import com.exam.exam_management_system.services.ExamScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

// Design Pattern: Facade - Facade class
@Component
public class ExamRegistrationFacade {
    // Architecture Pattern: Service Layer Pattern - Service injection
    private final ExamService examService;
    
    // Architecture Pattern: Service Layer Pattern - Service injection
    private final ExamRegistrationService registrationService;
    
    // Architecture Pattern: Service Layer Pattern - Service injection
    private final ExamScheduleService scheduleService;

    // Design Pattern: Facade - Constructor with service dependencies
    @Autowired
    public ExamRegistrationFacade(
            ExamService examService,
            ExamRegistrationService registrationService,
            ExamScheduleService scheduleService) {
        this.examService = examService;
        this.registrationService = registrationService;
        this.scheduleService = scheduleService;
    }

    // Design Pattern: Facade - Simplified method for complex registration process
    public RegistrationResult registerStudentForExam(Student student, Long examId) throws Exception {
        // 1. Get the exam
        Exam exam = examService.getExamById(examId);
        
        // 2. Validate exam is available for registration
        if (!exam.isOnline()) {
            throw new Exception("This exam is not available for online registration");
        }
        
        // 3. Create registration
        ExamRegistration registration = new ExamRegistration();
        registration.setStudent(student);
        registration.setExam(exam);
        registration.setRegistrationDate(LocalDateTime.now());
        
        // 4. Save registration
        registration = registrationService.saveRegistration(registration);
        
        // 5. Create schedule (5 minutes after registration)
        ExamSchedule schedule = new ExamSchedule();
        schedule.setExam(exam);
        schedule.setStartTime(LocalDateTime.now().plusMinutes(5));
        schedule.setExamDate(schedule.getStartTime().toLocalDate());
        schedule.setRegistration(registration);
        
        // 6. Save schedule
        schedule = scheduleService.saveSchedule(schedule);
        
        // 7. Return result
        return new RegistrationResult(registration, schedule);
    }

    // Design Pattern: Facade - Simplified method for updating multiple schedules
    public void updateSchedules(List<Long> registrationIds, LocalDateTime newStartTime, ExamCoordinator coordinator) throws Exception {
        for (Long registrationId : registrationIds) {
            ExamRegistration registration = registrationService.getRegistrationById(registrationId);
            if (registration == null) {
                throw new Exception("Registration not found: " + registrationId);
            }

            ExamSchedule schedule = registration.getSchedule();
            if (schedule == null) {
                throw new Exception("No schedule found for registration: " + registrationId);
            }

            // Update schedule
            schedule.setStartTime(newStartTime);
            schedule.setCoordinator(coordinator);
            scheduleService.saveSchedule(schedule);

            // Update exam date if this is the first schedule
            Exam exam = registration.getExam();
            if (exam.getExamDate() == null || exam.getExamDate().isAfter(newStartTime)) {
                exam.setExamDate(newStartTime);
                examService.saveExam(exam);
            }
        }
    }
} 