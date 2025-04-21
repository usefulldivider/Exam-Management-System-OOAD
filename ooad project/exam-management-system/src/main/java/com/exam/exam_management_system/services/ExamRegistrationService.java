package com.exam.exam_management_system.services;

import com.exam.exam_management_system.models.ExamRegistration;

public interface ExamRegistrationService {
    void registerStudentForExam(Long examId, String username) throws Exception;
    ExamRegistration saveRegistration(ExamRegistration registration);
    ExamRegistration getRegistrationById(Long id);
} 