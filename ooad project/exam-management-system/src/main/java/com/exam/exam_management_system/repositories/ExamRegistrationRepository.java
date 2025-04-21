package com.exam.exam_management_system.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import com.exam.exam_management_system.models.*;

@Repository
public interface ExamRegistrationRepository extends JpaRepository<ExamRegistration, Long> {
    List<ExamRegistration> findByStudent(Student student);
    
    List<ExamRegistration> findByExam(Exam exam);
    
    boolean existsByStudentAndExam(Student student, Exam exam);
}