package com.exam.exam_management_system.repositories;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import com.exam.exam_management_system.models.*;

public interface ExamScheduleRepository extends JpaRepository<ExamSchedule, Long> {
    List<ExamSchedule> findByExam(Exam exam);
}