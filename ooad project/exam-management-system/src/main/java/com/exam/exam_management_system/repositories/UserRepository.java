// Architecture Pattern: Repository Layer Pattern - Repository Interface
// Design Pattern: Repository - Data access abstraction layer

package com.exam.exam_management_system.repositories;

import com.exam.exam_management_system.models.User;
import com.exam.exam_management_system.models.enums.AccountStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

// Architecture Pattern: Repository Layer Pattern - Repository Interface
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Architecture Pattern: Repository Layer Pattern - Repository method
    Optional<User> findByUsername(String username);
    
    // Architecture Pattern: Repository Layer Pattern - Repository method
    Optional<User> findByUsernameAndPassword(String username, String password);
    
    // Architecture Pattern: Repository Layer Pattern - Repository method
    boolean existsByUsername(String username);
    
    // Architecture Pattern: Repository Layer Pattern - Repository method
    List<User> findByStatus(AccountStatus status);
}