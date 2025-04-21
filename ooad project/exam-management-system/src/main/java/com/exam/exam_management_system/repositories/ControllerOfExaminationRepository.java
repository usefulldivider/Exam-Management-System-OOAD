package com.exam.exam_management_system.repositories;

import com.exam.exam_management_system.models.ControllerOfExamination;
import com.exam.exam_management_system.models.Exam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ControllerOfExaminationRepository extends JpaRepository<ControllerOfExamination, Long> {
    List<ControllerOfExamination> findByLastExamDistributionAfter(LocalDateTime date);
    List<ControllerOfExamination> findByLastResultsPublicationAfter(LocalDateTime date);
    List<ControllerOfExamination> findByManagedExamsContaining(Exam exam);
}
