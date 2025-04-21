package com.exam.exam_management_system.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import com.exam.exam_management_system.models.*;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {
}