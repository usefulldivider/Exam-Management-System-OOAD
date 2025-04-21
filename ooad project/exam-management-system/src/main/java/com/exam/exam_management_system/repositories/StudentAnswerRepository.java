package com.exam.exam_management_system.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import com.exam.exam_management_system.models.*;


@Repository
public interface StudentAnswerRepository extends JpaRepository<StudentAnswer, Long> {
    

    // Custom query to find answers by exam ID and student ID
    @Query("SELECT sa FROM StudentAnswer sa JOIN sa.question q WHERE q.exam.id = :examId AND sa.student.id = :studentId")
    List<StudentAnswer> findByExamIdAndStudentId(@Param("examId") Long examId, @Param("studentId") Long studentId);

}