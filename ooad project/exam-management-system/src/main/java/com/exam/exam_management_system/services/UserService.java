// Architecture Pattern: Service Layer Pattern - Service Interface
// Design Pattern: Facade - Provides a simplified interface to user management operations

package com.exam.exam_management_system.services;

import com.exam.exam_management_system.models.User;
import java.util.List;

// Architecture Pattern: Service Layer Pattern - Service Interface
public interface UserService {
    // Architecture Pattern: Service Layer Pattern - Service method
    User authenticate(String username, String password);
    
    // Architecture Pattern: Service Layer Pattern - Service method
    User createUser(User user);
    
    // Architecture Pattern: Service Layer Pattern - Service method
    User findByUsername(String username);
    
    // Architecture Pattern: Service Layer Pattern - Service method
    boolean existsByUsername(String username);
    
    // Architecture Pattern: Service Layer Pattern - Service method
    User findOrCreateCoordinator();
    
    // Architecture Pattern: Service Layer Pattern - Service method
    User findOrCreateController();
    
    // Architecture Pattern: Service Layer Pattern - Service method
    List<User> getPendingRegistrations();
    
    // Architecture Pattern: Service Layer Pattern - Service method
    User getUserById(Long id);
    
    // Architecture Pattern: Service Layer Pattern - Service method
    User updateUser(User user);
    
    // Architecture Pattern: Service Layer Pattern - Service method
    List<User> getUsersByRole(String role);
} 