package com.exam.exam_management_system.exam;

import com.exam.exam_management_system.models.Exam;
import com.exam.exam_management_system.models.Student;
import com.exam.exam_management_system.services.ExamService;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public abstract class ExamSession {
    protected final Exam exam;
    protected final Student student;
    protected final ExamService examService;
    protected LocalDateTime startTime;
    protected boolean isCompleted = false;

    public ExamSession(Exam exam, Student student, ExamService examService) {
        this.exam = exam;
        this.student = student;
        this.examService = examService;
    }

    // Template method - defines the algorithm structure
    public final void conductExam() {
        if (!canStartExam()) {
            throw new IllegalStateException("Exam cannot be started at this time");
        }

        startTime = LocalDateTime.now();
        initializeExam();
        
        while (!isTimeUp() && !isCompleted) {
            processAnswer();
        }
        
        if (isTimeUp()) {
            forceSubmit();
        }
        
        evaluateExam();
    }

    // Hook methods - can be overridden by subclasses
    protected void initializeExam() {
        // Default implementation - can be overridden
    }

    protected abstract void processAnswer();
    protected abstract void evaluateExam();

    // Concrete methods - shared by all subclasses
    public boolean canStartExam() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime examStart = exam.getExamDate();
        LocalDateTime examEnd = examStart.plusDays(1);
        
        return now.isAfter(examStart) && now.isBefore(examEnd);
    }

    protected boolean isTimeUp() {
        if (startTime == null) return false;
        
        LocalDateTime now = LocalDateTime.now();
        long minutesElapsed = ChronoUnit.MINUTES.between(startTime, now);
        return minutesElapsed >= exam.getDuration();
    }

    protected void forceSubmit() {
        isCompleted = true;
        // Save any pending answers
        saveCurrentAnswer();
    }

    protected abstract void saveCurrentAnswer();
} 