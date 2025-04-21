package com.exam.exam_management_system.services.impl;

import com.exam.exam_management_system.models.Exam;
import com.exam.exam_management_system.models.ExamRegistration;
import com.exam.exam_management_system.models.Student;
import com.exam.exam_management_system.repositories.ExamRegistrationRepository;
import com.exam.exam_management_system.repositories.ExamRepository;
import com.exam.exam_management_system.repositories.UserRepository;
import com.exam.exam_management_system.services.ExamRegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class ExamRegistrationServiceImpl implements ExamRegistrationService {

    @Autowired
    private ExamRepository examRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ExamRegistrationRepository registrationRepository;

    @Override
    @Transactional
    public void registerStudentForExam(Long examId, String username) throws Exception {
        Exam exam = examRepository.findById(examId)
                .orElseThrow(() -> new Exception("Exam not found"));

        if (!exam.isOnline()) {
            throw new Exception("This exam is not available for online registration");
        }

        Student student = (Student) userRepository.findByUsername(username)
                .orElseThrow(() -> new Exception("Student not found"));

        // Check if student is already registered
        if (registrationRepository.existsByStudentAndExam(student, exam)) {
            throw new Exception("You are already registered for this exam");
        }

        ExamRegistration registration = new ExamRegistration();
        registration.setExam(exam);
        registration.setStudent(student);
        registration.setRegistrationDate(LocalDateTime.now());

        registrationRepository.save(registration);
    }

    @Override
    @Transactional
    public ExamRegistration saveRegistration(ExamRegistration registration) {
        return registrationRepository.save(registration);
    }

    @Override
    public ExamRegistration getRegistrationById(Long id) {
        Optional<ExamRegistration> registration = registrationRepository.findById(id);
        return registration.orElse(null);
    }
} 