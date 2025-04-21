package com.exam.exam_management_system.models;

public class RegistrationResult {
    private final ExamRegistration registration;
    private final ExamSchedule schedule;

    public RegistrationResult(ExamRegistration registration, ExamSchedule schedule) {
        this.registration = registration;
        this.schedule = schedule;
    }

    public ExamRegistration getRegistration() {
        return registration;
    }

    public ExamSchedule getSchedule() {
        return schedule;
    }
} 