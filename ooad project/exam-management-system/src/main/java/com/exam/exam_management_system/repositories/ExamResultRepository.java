package com.exam.exam_management_system.repositories;

import com.exam.exam_management_system.models.Exam;
import com.exam.exam_management_system.models.ExamResult;
import com.exam.exam_management_system.models.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing ExamResult entities.
 */
@Repository
public interface ExamResultRepository extends JpaRepository<ExamResult, Long> {

    /**
     * Finds all exam results associated with a specific exam.
     *
     * @param exam The exam entity.
     * @return A list of exam results for the given exam.
     */
    List<ExamResult> findByExam(Exam exam);

    /**
     * Finds all exam results associated with a specific student.
     *
     * @param student The student entity.
     * @return A list of exam results for the given student.
     */
    List<ExamResult> findByStudent(Student student);

    /**
     * Finds a specific exam result for a given exam and student.
     *
     * @param exam The exam entity.
     * @param student The student entity.
     * @return An Optional containing the ExamResult if found, otherwise empty.
     */
    Optional<ExamResult> findByExamAndStudent(Exam exam, Student student);


}