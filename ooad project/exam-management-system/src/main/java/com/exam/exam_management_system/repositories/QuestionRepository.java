package com.exam.exam_management_system.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import com.exam.exam_management_system.models.*;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {

}
