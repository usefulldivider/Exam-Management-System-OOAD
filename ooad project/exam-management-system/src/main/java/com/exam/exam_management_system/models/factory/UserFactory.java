// Design Pattern: Factory - Factory class for creating different types of users

package com.exam.exam_management_system.models.factory;

import com.exam.exam_management_system.models.*;
import com.exam.exam_management_system.models.enums.UserRole;

// Design Pattern: Factory - Factory class
public class UserFactory {
    // Design Pattern: Factory - Factory method for creating user objects
    public static User createUser(UserRole role, String username, String password, String name, String email, String additional) {
        return switch (role) {
            case STUDENT -> new Student(username, password, name, email, additional);
            case TEACHER -> new Teacher(username, password, name, email, additional);
            case EXAM_COORDINATOR -> new ExamCoordinator(username, password, name, email, additional);
            case CONTROLLER_OF_EXAMINATION -> new ControllerOfExamination(username, password, name, email, additional);
            default -> throw new IllegalArgumentException("Invalid role");
        };
    }
}