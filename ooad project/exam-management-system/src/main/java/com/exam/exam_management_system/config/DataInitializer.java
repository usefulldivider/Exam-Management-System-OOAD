package com.exam.exam_management_system.config;

import com.exam.exam_management_system.models.*;
import com.exam.exam_management_system.models.enums.AccountStatus;
import com.exam.exam_management_system.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        // Initialize default users if they don't exist
        initializeDefaultUsers();
        
        // TEST USERS - REMOVE BEFORE PRODUCTION
        initializeTestUsers();
    }

    private void initializeDefaultUsers() {
        // Exam Coordinator
        if (!userRepository.existsByUsername("exam_coordinator")) {
            ExamCoordinator coordinator = new ExamCoordinator(
                "exam_coordinator",
                "coordinator123",
                "Exam Coordinator",
                "coordinator@example.com"
            );
            userRepository.save(coordinator);
        }

        // Controller of Examination
        if (!userRepository.existsByUsername("controller")) {
            ControllerOfExamination controller = new ControllerOfExamination(
                "controller",
                "controller123",
                "Controller of Examination",
                "controller@example.com"
            );
            userRepository.save(controller);
        }
    }

    // TEST USERS - REMOVE BEFORE PRODUCTION
    private void initializeTestUsers() {
        // Test Teacher
        if (!userRepository.existsByUsername("tea")) {
            Teacher teacher = new Teacher(
                "tea",
                "abc",
                "Test Teacher",
                "teacher@example.com",
                "Computer Science"
            );
            teacher.setStatus(AccountStatus.APPROVED);
            userRepository.save(teacher);
        }

        // Test Student
        if (!userRepository.existsByUsername("stu")) {
            Student student = new Student(
                "stu",
                "abc",
                "Test Student",
                "student@example.com",
                "CS2023001"
            );
            student.setStatus(AccountStatus.APPROVED);
            userRepository.save(student);
        }
    }
} 