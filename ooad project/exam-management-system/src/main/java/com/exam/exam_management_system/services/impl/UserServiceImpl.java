// Architecture Pattern: Service Layer Pattern - Service Implementation
// Design Pattern: Repository Pattern - Uses UserRepository for data access
// Design Pattern: Factory - Creates different types of users (Coordinator, Controller)

package com.exam.exam_management_system.services.impl;

import com.exam.exam_management_system.models.User;
import com.exam.exam_management_system.models.ExamCoordinator;
import com.exam.exam_management_system.models.ControllerOfExamination;
import com.exam.exam_management_system.models.enums.UserRole;
import com.exam.exam_management_system.models.enums.AccountStatus;
import com.exam.exam_management_system.repositories.UserRepository;
import com.exam.exam_management_system.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

// Architecture Pattern: Service Layer Pattern - Service Implementation
@Service
@Transactional
public class UserServiceImpl implements UserService {

    // Architecture Pattern: Repository Pattern - Repository injection
    @Autowired
    private UserRepository userRepository;

    // Architecture Pattern: Service Layer Pattern - Service method implementation
    @Override
    public User authenticate(String username, String password) {
        Optional<User> userOpt = userRepository.findByUsernameAndPassword(username, password);
        return userOpt.orElse(null);
    }

    // Architecture Pattern: Service Layer Pattern - Service method implementation
    @Override
    public User createUser(User user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username already exists");
        }
        return userRepository.save(user);
    }

    // Architecture Pattern: Service Layer Pattern - Service method implementation
    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElse(null);
    }

    // Architecture Pattern: Service Layer Pattern - Service method implementation
    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    // Design Pattern: Factory - Creates ExamCoordinator instance
    @Override
    public User findOrCreateCoordinator() {
        User coordinator = findByUsername("exam_coordinator");
        if (coordinator == null) {
            ExamCoordinator newCoordinator = new ExamCoordinator();
            newCoordinator.setUsername("exam_coordinator");
            newCoordinator.setPassword("coordinator123");
            newCoordinator.setName("Exam Coordinator");
            newCoordinator.setEmail("coordinator@exam.com");
            newCoordinator.setRole(UserRole.EXAM_COORDINATOR);
            newCoordinator.setStatus(AccountStatus.APPROVED);
            coordinator = createUser(newCoordinator);
        }
        return coordinator;
    }

    // Design Pattern: Factory - Creates ControllerOfExamination instance
    @Override
    public User findOrCreateController() {
        User controller = findByUsername("exam_controller");
        if (controller == null) {
            ControllerOfExamination newController = new ControllerOfExamination();
            newController.setUsername("exam_controller");
            newController.setPassword("controller123");
            newController.setName("Controller of Examination");
            newController.setEmail("controller@exam.com");
            newController.setRole(UserRole.CONTROLLER_OF_EXAMINATION);
            newController.setStatus(AccountStatus.APPROVED);
            controller = createUser(newController);
        }
        return controller;
    }

    // Architecture Pattern: Service Layer Pattern - Service method implementation
    @Override
    public List<User> getPendingRegistrations() {
        return userRepository.findByStatus(AccountStatus.PENDING);
    }

    // Architecture Pattern: Service Layer Pattern - Service method implementation
    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    // Architecture Pattern: Service Layer Pattern - Service method implementation
    @Override
    public User updateUser(User user) {
        return userRepository.save(user);
    }

    // Architecture Pattern: Service Layer Pattern - Service method implementation
    @Override
    public List<User> getUsersByRole(String role) {
        return userRepository.findAll().stream()
                .filter(user -> user.getRole().toString().equals(role))
                .collect(Collectors.toList());
    }
} 