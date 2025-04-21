package com.exam.exam_management_system.repositories;

import com.exam.exam_management_system.models.ExamCoordinator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ExamCoordinatorRepository extends JpaRepository<ExamCoordinator, Long> {
    Optional<ExamCoordinator> findByUsername(String username);
}
