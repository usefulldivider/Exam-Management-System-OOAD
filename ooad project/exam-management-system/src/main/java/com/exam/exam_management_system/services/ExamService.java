package com.exam.exam_management_system.services;

import com.exam.exam_management_system.models.*;
import com.exam.exam_management_system.models.enums.ExamState;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface ExamService {
    // Teacher methods
    List<Exam> getExamsForTeacher(User teacher);
    int getExamCountForTeacher(User teacher);
    Exam getExamById(Long id);
    Exam createExam(Exam exam, User teacher);
    Exam updateExam(Long id, String subject, LocalDateTime examDate, int duration);
    void publishExam(Long id);
    List<Map<String, Object>> getExamResults(Long examId);

    // Student methods
    List<Exam> getAvailableExams(User student);
    int getAvailableExamsCount(User student);
    List<Exam> getUpcomingExams(User student);
    int getUpcomingExamsCount(User student);
    List<ExamRegistration> getStudentRegistrations(Student student);
    boolean isStudentRegistered(Exam exam, Student student);

    // Exam Creation and Management
    void deleteExam(Long examId);
    Exam getExam(Long examId);
    List<Exam> getExamsByState(ExamState state);
    List<Exam> getExamsByCreator(Teacher creator);
    void saveExam(Exam exam);
    
    // Question Management
    Question addQuestion(Long examId, String questionText, String correctAnswer, int marks);
    void removeQuestion(Long questionId);
    List<Question> getQuestions(Long examId);
    Question getQuestionById(Long questionId);
    Question addQuestionToExam(Long examId, Question question, User teacher);
    
    // Exam Registration
    ExamRegistration registerForExam(Student student, Exam exam);
    List<ExamRegistration> getAllRegistrations();
    
    // Exam Schedule
    ExamSchedule createSchedule(Exam exam, LocalDate examDate, LocalDateTime startTime, ExamCoordinator coordinator);
    ExamSchedule updateSchedule(Long scheduleId, LocalDate examDate, LocalDateTime startTime);
    List<ExamSchedule> getSchedulesByExam(Exam exam);
    
    // Exam State Management
    void submitExamForProofreading(Long examId);
    void approveProofreading(Exam exam);
    void publishExam(Exam exam, ControllerOfExamination controller);
    void startExam(Exam exam);
    void completeExam(Exam exam);
    void evaluateExam(Exam exam);
    
    // Results Management
    void gradeExam(Long examId);
    StudentAnswer saveStudentAnswer(StudentAnswer answer);
    StudentAnswer getStudentAnswer(Long answerId);
    List<StudentAnswer> getStudentAnswers(Long examId, Student student);
    void publishResults(Exam exam, ControllerOfExamination controller);
    List<ExamResult> getExamResultsByStudent(Student student);
    ExamResult getExamResult(Long resultId);
    void saveExamResult(ExamResult result);
    
    // Additional Methods
    List<Exam> getPublishedExams();
    List<Exam> getDraftExams(User teacher);
    List<Exam> getExamsPendingProofread(User currentTeacher);
    void processProofreadSubmission(Long examId, User proofreader, List<Question> submittedQuestions);
    List<Exam> getStoredExamPapers();
    List<Exam> getAvailableOnlineExams();
    List<Exam> getActiveOnlineExams();
    ExamResult getExamResultByExamAndStudent(Exam exam, Student student);
} 