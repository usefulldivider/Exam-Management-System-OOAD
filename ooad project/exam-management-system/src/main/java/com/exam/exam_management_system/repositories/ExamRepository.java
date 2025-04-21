package com.exam.exam_management_system.repositories;

import com.exam.exam_management_system.models.Exam;
import com.exam.exam_management_system.models.User;
import com.exam.exam_management_system.models.enums.ExamState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ExamRepository extends JpaRepository<Exam, Long> {
    List<Exam> findByTeacher(User teacher);
    int countByTeacher(User teacher);
    List<Exam> findByStateAndExamDateAfter(ExamState state, LocalDateTime date);
    List<Exam> findByState(ExamState state);
    List<Exam> findByTeacherAndState(User teacher, ExamState state);
    List<Exam> findByStateAndCreatorNot(ExamState state, User creator);

    List<Exam> findByStateOrderByExamDateDesc(ExamState state);
}