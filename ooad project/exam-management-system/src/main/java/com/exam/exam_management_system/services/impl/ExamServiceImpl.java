package com.exam.exam_management_system.services.impl;

import com.exam.exam_management_system.models.*;
import com.exam.exam_management_system.models.enums.*;
import com.exam.exam_management_system.repositories.*;
import com.exam.exam_management_system.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.HashMap;

@Service
@Transactional
public class ExamServiceImpl implements ExamService {

    @Autowired
    private ExamRepository examRepository;

    // @Autowired
    // private NotificationService notificationService;

    @Autowired
    private StudentAnswerRepository studentAnswerRepository;


    @Autowired
    private ExamRegistrationRepository examRegistrationRepository;

    // @Autowired
    // private ExamCoordinatorRepository examCoordinatorRepository;


    // @Autowired
    // private ControllerOfExaminationRepository controllerOfExaminationRepository;

    @Autowired
    private ExamScheduleRepository examScheduleRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private ExamResultRepository examResultRepository;

    // @Autowired
    // private UserService userService;

    @Override
    public List<Exam> getExamsForTeacher(User teacher) {
        return examRepository.findByTeacher(teacher);
    }

    @Override
    public int getExamCountForTeacher(User teacher) {
        return examRepository.countByTeacher(teacher);
    }

    @Override
    public Exam getExamById(Long id) {
        return examRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Exam not found with id: " + id));
    }

    @Override
    @Transactional
    public Exam createExam(Exam exam, User teacher) {
        if (!(teacher instanceof Teacher)) {
            throw new IllegalArgumentException("User creating/updating exam must be a Teacher");
        }

        // Use builder to create the exam
        ExamBuilder builder = new ExamBuilder()
            .setSubject(exam.getSubject())
            .setExamDate(exam.getExamDate())
            .setDuration(exam.getDuration())
            .setTotalMarks(exam.getTotalMarks())
            .setTotalQuestions(exam.getTotalQuestions())
            .setTeacher(teacher)
            .setCreator((Teacher) teacher)
            .setShowResults(exam.isShowResults())
            .setAllowReview(exam.isAllowReview())
            .setOnline(exam.isOnline())
            .setState(ExamState.DRAFT);

        // Add questions if any
        if (exam.getQuestions() != null) {
            for (Question question : exam.getQuestions()) {
                builder.addQuestion(question);
            }
        }

        Exam newExam = builder.build();
        return examRepository.save(newExam);
    }

    @Override
    @Transactional
    public void publishExam(Long id) {
        Exam exam = getExamById(id);
        if (exam.getState() == ExamState.DRAFT || exam.getState() == ExamState.PROOFREAD) {
            exam.setState(ExamState.STORED_IN_COE);
            examRepository.save(exam);
        } else {
            throw new IllegalStateException("Exam cannot be published from state: " + exam.getState());
        }
    }

    @Override
    public List<Map<String, Object>> getExamResults(Long examId) {
        Exam exam = getExamById(examId);
        List<ExamResult> results = examResultRepository.findByExam(exam);
        
        return results.stream().map(result -> {
            Map<String, Object> map = new HashMap<>();
            map.put("studentId", result.getStudent().getId());
            map.put("studentName", result.getStudent().getName());
            map.put("marksObtained", result.getTotalMarks());
            map.put("totalMarks", result.getExam().getTotalMarks());
            map.put("status", result.getStatus());
            map.put("submissionTime", result.getSubmissionTime());
            return map;
        }).collect(Collectors.toList());
    }

    @Override
    public List<Exam> getAvailableExams(User student) {
        return examRepository.findByState(ExamState.STORED_IN_COE);
    }

    @Override
    public int getAvailableExamsCount(User student) {
        return getAvailableExams(student).size();
    }

    @Override
    public List<Exam> getUpcomingExams(User student) {
        return examRepository.findByStateAndExamDateAfter(ExamState.STORED_IN_COE, LocalDateTime.now());
    }

    @Override
    public int getUpcomingExamsCount(User student) {
        return getUpcomingExams(student).size();
    }

    @Override
    public List<ExamRegistration> getStudentRegistrations(Student student) {
        return examRegistrationRepository.findByStudent(student);
    }

    @Override
    public void deleteExam(Long id) {
        examRepository.deleteById(id);
    }

    @Override
    public Exam getExam(Long examId) {
        return getExamById(examId);
    }

    @Override
    public List<Exam> getExamsByState(ExamState state) {
        return examRepository.findByState(state);
    }

    @Override
    public List<Exam> getExamsByCreator(Teacher creator) {
        return examRepository.findByTeacher(creator);
    }

    @Override
    public void saveExam(Exam exam) {
        examRepository.save(exam);
    }

    @Override
    @Transactional
    public Question addQuestionToExam(Long examId, Question question, User teacher) {
        Exam exam = getExamById(examId);
        if (exam.getState() != ExamState.DRAFT) {
            throw new IllegalStateException("Questions can only be added to exams in DRAFT state.");
        }
        if (!exam.getTeacher().getId().equals(teacher.getId())) {
             throw new SecurityException("User is not authorized to add questions to this exam.");
        }
        
        // Create a new exam with existing properties using builder
        ExamBuilder builder = new ExamBuilder()
            .setSubject(exam.getSubject())
            .setExamDate(exam.getExamDate())
            .setDuration(exam.getDuration())
            .setTotalMarks(exam.getTotalMarks())
            .setTotalQuestions(exam.getTotalQuestions())
            .setTeacher(exam.getTeacher())
            .setCreator((Teacher) exam.getTeacher())
            .setShowResults(exam.isShowResults())
            .setAllowReview(exam.isAllowReview())
            .setOnline(exam.isOnline())
            .setState(exam.getState());
        
        // Add existing questions
        if (exam.getQuestions() != null) {
            for (Question q : exam.getQuestions()) {
                builder.addQuestion(q);
            }
        }
        
        // Add the new question
        builder.addQuestion(question);
        
        // Build the updated exam
        Exam updatedExam = builder.build();
        updatedExam.setId(exam.getId()); // Preserve the ID
        
        // Save the question first to get an ID
        question.setExam(updatedExam);
        Question savedQuestion = questionRepository.save(question);
        
        // Save the updated exam
        examRepository.save(updatedExam);
        
        return savedQuestion;
    }

    @Override
    public Question addQuestion(Long examId, String questionText, String answer, int marks) {
        Exam exam = getExamById(examId);
        Question question = new Question();
        question.setQuestionText(questionText);
        question.setOptionA("Placeholder A");
        question.setOptionB("Placeholder B");
        question.setOptionC("Placeholder C");
        question.setOptionD("Placeholder D");
        try {
            question.setCorrectAnswer(MCQOption.valueOf(answer.toUpperCase()));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid correct answer option provided: " + answer);
        }
        question.setMarks(marks);
        
        // Use addQuestionToExam which now uses the builder pattern internally
        return addQuestionToExam(examId, question, exam.getTeacher());
    }

    @Override
    public List<Question> getQuestions(Long examId) {
        Exam exam = getExamById(examId);
        return new ArrayList<>(exam.getQuestions() != null ? exam.getQuestions() : new ArrayList<>());
    }

    @Override
    public void removeQuestion(Long questionId) {
        Question question = questionRepository.findById(questionId)
             .orElseThrow(() -> new RuntimeException("Question not found: " + questionId));
        Exam exam = question.getExam();
        if (exam.getState() != ExamState.DRAFT) {
             throw new IllegalStateException("Cannot remove questions from exams not in DRAFT state.");
        }
        exam.getQuestions().remove(question);
        questionRepository.deleteById(questionId);
        examRepository.save(exam);
    }

    @Override
    @Transactional
    public ExamRegistration registerForExam(Student student, Exam exam) {
        if (examRegistrationRepository.existsByStudentAndExam(student, exam)) {
            throw new IllegalStateException("Student already registered for this exam.");
        }
        if (exam.getState() != ExamState.STORED_IN_COE) {
            throw new IllegalStateException("Cannot register for an exam that is not stored in COE.");
        }
        
        ExamRegistration registration = new ExamRegistration();
        registration.setStudent(student);
        registration.setExam(exam);
        registration.setRegistrationDate(LocalDateTime.now());
        
        return examRegistrationRepository.save(registration);
    }

    @Override
    public List<ExamRegistration> getAllRegistrations() {
        return examRegistrationRepository.findAll();
    }

    @Override
    public ExamSchedule createSchedule(Exam exam, LocalDate examDate, LocalDateTime startTime, ExamCoordinator coordinator) {
        // Add checks: Does exam exist? Is it in a state that can be scheduled?
        ExamSchedule schedule = new ExamSchedule();
        schedule.setExam(exam);
        schedule.setExamDate(examDate);
        schedule.setStartTime(startTime);    
        schedule.setCoordinator(coordinator); // Set the responsible coordinator
        
        // Update Exam's date/time if this is the definitive schedule
        exam.setExamDate(startTime);
        examRepository.save(exam);
        
        return examScheduleRepository.save(schedule);
    }

    @Override
    public ExamSchedule updateSchedule(Long scheduleId, LocalDate examDate, LocalDateTime startTime) {
        ExamSchedule schedule = examScheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new RuntimeException("Schedule not found"));
        
        schedule.setExamDate(examDate);
        schedule.setStartTime(startTime);

        
        schedule.getExam().setExamDate(startTime);
        examRepository.save(schedule.getExam());
        
        return examScheduleRepository.save(schedule);
    }

    @Override
    public List<ExamSchedule> getSchedulesByExam(Exam exam) {
        return examScheduleRepository.findByExam(exam);
    }



    @Override
    public void approveProofreading(Exam exam) {
        if (exam.getState() == ExamState.PROOFREAD) {
            examRepository.save(exam);
        } else {
             throw new IllegalStateException("Proofreading cannot be approved from state: " + exam.getState());
        }
    }

    @Override
    public void publishExam(Exam exam, ControllerOfExamination controller) {
        if (exam.getState() == ExamState.EVALUATED) {
            exam.setPublishedBy(controller);
            exam.setState(ExamState.RESULTS_PUBLISHED);
            examRepository.save(exam);
        } else {
            throw new IllegalStateException("Exam results cannot be published from state: " + exam.getState());
        }
    }

    @Override
    public void startExam(Exam exam) {
        if (exam.getState() == ExamState.STORED_IN_COE) {
            exam.setState(ExamState.ACTIVE);
            examRepository.save(exam);
        } else {
            throw new IllegalStateException("Exam cannot be started from state: " + exam.getState());
        }
    }

    @Override
    public void completeExam(Exam exam) {
         if (exam.getState() == ExamState.ACTIVE) {
             exam.setState(ExamState.COMPLETED);
             examRepository.save(exam);
         } else {
             throw new IllegalStateException("Exam cannot be completed from state: " + exam.getState());
         }
    }

    @Override
    public void evaluateExam(Exam exam) {
        if (exam.getState() == ExamState.COMPLETED) {
             exam.setState(ExamState.EVALUATED);
             examRepository.save(exam);
         } else {
             throw new IllegalStateException("Exam cannot be evaluated from state: " + exam.getState());
         }
    }

    @Override
    public void gradeExam(Long examId) {
        System.out.println("Placeholder: Grading logic for examId " + examId + " would go here.");
    }

    @Override
    public StudentAnswer saveStudentAnswer(StudentAnswer answer) {
        return studentAnswerRepository.save(answer);
    }

    @Override
    public void submitExamForProofreading(Long examId) {
        Exam exam = getExamById(examId);
        if (exam.getState() == ExamState.DRAFT) {
            exam.setState(ExamState.PROOFREAD);
            examRepository.save(exam);
        } else {
             throw new IllegalStateException("Exam cannot be submitted for proofreading from state: " + exam.getState());
        }
    }

    @Override
    public List<Exam> getExamsPendingProofread(User currentTeacher) {
        if (currentTeacher == null) {
            return new ArrayList<>();
        }
        return examRepository.findByStateAndCreatorNot(ExamState.PROOFREAD, currentTeacher);
    }

    @Override
    public List<Exam> getPublishedExams() {
        return examRepository.findByState(ExamState.STORED_IN_COE);
    }

    @Override
    public List<Exam> getDraftExams(User teacher) {
        if (teacher instanceof Teacher) {
            return examRepository.findByTeacherAndState((Teacher) teacher, ExamState.DRAFT);
        }
        return new ArrayList<>();
    }

    @Override
    public Exam updateExam(Long id, String subject, LocalDateTime examDate, int duration) {
        Exam existingExam = getExamById(id);
        existingExam.setSubject(subject);
        existingExam.setExamDate(examDate); 
        existingExam.setDuration(duration);
        return examRepository.save(existingExam);
    }

    @Override
    public void publishResults(Exam exam, ControllerOfExamination controller) {
        if (exam.getState() != ExamState.EVALUATED) {
            throw new IllegalStateException("Cannot publish results for exam in state: " + exam.getState());
        }
        
        exam.setState(ExamState.RESULTS_PUBLISHED);
        exam.setPublishedBy(controller);
        examRepository.save(exam);
    }

    @Override
    public List<ExamResult> getExamResultsByStudent(Student student) {
        return examResultRepository.findByStudent(student);
    }

    @Override
    public void saveExamResult(ExamResult result) {
        examResultRepository.save(result);
    }

    @Override
    public boolean isStudentRegistered(Exam exam, Student student) {
        return examRegistrationRepository.existsByStudentAndExam(student, exam);
    }

    @Override
    public Question getQuestionById(Long questionId) {
        return questionRepository.findById(questionId)
            .orElseThrow(() -> new RuntimeException("Question not found with id: " + questionId));
    }

    @Override
    public List<Exam> getStoredExamPapers() {
        return examRepository.findByState(ExamState.STORED_IN_COE);
    }

    @Override
    public List<Exam> getAvailableOnlineExams() {
        return examRepository.findByStateOrderByExamDateDesc(ExamState.ACTIVE);
    }

    @Override
    public List<Exam> getActiveOnlineExams() {
        return examRepository.findByStateOrderByExamDateDesc(ExamState.ACTIVE);
    }

    @Override
    public StudentAnswer getStudentAnswer(Long answerId) {
        return studentAnswerRepository.findById(answerId)
                .orElseThrow(() -> new RuntimeException("Student answer not found"));
    }

    @Override
    public List<StudentAnswer> getStudentAnswers(Long examId, Student student) {
        return studentAnswerRepository.findByExamIdAndStudentId(examId, student.getId());
    }

    @Override
    public void processProofreadSubmission(Long examId, User proofreader, List<Question> submittedQuestions) {
        Exam exam = getExamById(examId);
        
        // Validate exam state
        if (exam.getState() != ExamState.PROOFREAD) {
            throw new IllegalStateException("Exam is not in PROOFREAD state");
        }

        // Validate proofreader
        if (proofreader == null || !(proofreader instanceof Teacher)) {
            throw new IllegalArgumentException("Invalid proofreader");
        }

        // First, delete all existing questions
        if (exam.getQuestions() != null) {
            questionRepository.deleteAll(exam.getQuestions());
            exam.getQuestions().clear();
        }

        // Create new list of questions
        List<Question> newQuestions = new ArrayList<>();
        
        for (Question submittedQ : submittedQuestions) {
            // Basic validation on submitted data
            if (submittedQ.getQuestionText() == null || submittedQ.getQuestionText().isBlank() ||
                submittedQ.getOptionA() == null || submittedQ.getOptionA().isBlank() ||
                submittedQ.getOptionB() == null || submittedQ.getOptionB().isBlank() ||
                submittedQ.getOptionC() == null || submittedQ.getOptionC().isBlank() ||
                submittedQ.getOptionD() == null || submittedQ.getOptionD().isBlank() ||
                submittedQ.getCorrectAnswer() == null) {
                throw new IllegalArgumentException("Invalid data submitted for one or more questions.");   
            }
            
            Question newQ = new Question();
            newQ.setQuestionText(submittedQ.getQuestionText());
            newQ.setOptionA(submittedQ.getOptionA());
            newQ.setOptionB(submittedQ.getOptionB());
            newQ.setOptionC(submittedQ.getOptionC());
            newQ.setOptionD(submittedQ.getOptionD());
            newQ.setCorrectAnswer(submittedQ.getCorrectAnswer());
            newQ.setMarks(submittedQ.getMarks());
            newQ.setExam(exam);
            newQuestions.add(newQ);
        }

        // Save all new questions first
        questionRepository.saveAll(newQuestions);
        
        // Update exam with new questions
        exam.setQuestions(newQuestions);
        exam.setProofreader(proofreader);
        exam.setState(ExamState.STORED_IN_COE);

        // Save exam with updated questions
        examRepository.save(exam);
    }

    @Override
    public ExamResult getExamResult(Long resultId) {
        return examResultRepository.findById(resultId)
            .orElseThrow(() -> new RuntimeException("Exam result not found: " + resultId));
    }

    @Override
    public ExamResult getExamResultByExamAndStudent(Exam exam, Student student) {
        return examResultRepository.findByExamAndStudent(exam, student).orElse(null);
    }
} 