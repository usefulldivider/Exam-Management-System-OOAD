// Architecture Pattern: MVC - Controller (Authentication Controller)
// Design Pattern: Factory - Uses UserFactory for user creation

package com.exam.exam_management_system.controllers;

import com.exam.exam_management_system.models.*;
import com.exam.exam_management_system.models.enums.UserRole;
import com.exam.exam_management_system.models.enums.AccountStatus;
import com.exam.exam_management_system.models.factory.UserFactory;
import com.exam.exam_management_system.services.UserService;
import com.exam.exam_management_system.services.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;

// Architecture Pattern: MVC - Controller
@Controller
public class AuthController {

    // Architecture Pattern: Service Layer Pattern - Service injection
    @Autowired
    private UserService userService;

    // Architecture Pattern: Service Layer Pattern - Service injection
    @Autowired
    private NotificationService notificationService;

    // Architecture Pattern: MVC - Controller endpoint
    @GetMapping("/")
    public String showRegisterForm() {
        return "auth/register";
    }

    // Architecture Pattern: MVC - Controller endpoint
    @GetMapping("/register")
    public String showRegistrationForm() {
        return "auth/register";
    }

    // Architecture Pattern: MVC - Controller endpoint
    @PostMapping("/register")
    public String registerUser(
            @RequestParam UserRole role,
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam String name,
            @RequestParam String email,
            @RequestParam(required = false) String additional,
            Model model) {
        
        // Only allow student and teacher registration
        if (role != UserRole.STUDENT && role != UserRole.TEACHER) {
            model.addAttribute("error", "Only students and teachers can register");
            return "auth/register";
        }

        // Check if username already exists
        if (userService.existsByUsername(username)) {
            model.addAttribute("error", "Username already exists");
            return "auth/register";
        }

        try {
            // Design Pattern: Factory - Creating user objects
            User user = UserFactory.createUser(role, username, password, name, email, additional);
            user.setStatus(AccountStatus.PENDING);
            userService.createUser(user);

            // Architecture Pattern: Service Layer Pattern - Using notification service
            User coordinator = userService.findByUsername("exam_coordinator");
            if (coordinator != null) {
                notificationService.sendNotification(coordinator, 
                    "New " + role.toString().toLowerCase() + " registration: " + username);
            }

            model.addAttribute("message", "Registration successful! Your account is pending approval. Please check back after 24 hours.");
            return "auth/login";
        } catch (Exception e) {
            model.addAttribute("error", "Registration failed: " + e.getMessage());
            return "auth/register";
        }
    }

    // Architecture Pattern: MVC - Controller endpoint
    @GetMapping("/login")
    public String showLoginForm(@RequestParam(value = "registered", required = false) boolean registered, Model model) {
        if (registered) {
            model.addAttribute("message", "Registration successful! Your account is pending approval. Please check back after 24 hours.");
        }
        return "auth/login";
    }

    // Architecture Pattern: MVC - Controller endpoint
    @PostMapping("/login")
    public String loginUser(
            @RequestParam String username,
            @RequestParam String password,
            HttpSession session,
            Model model) {
        
        // Check for predefined coordinator credentials
        if (username.equals("exam_coordinator") && password.equals("coordinator123")) {
            User coordinator = userService.findOrCreateCoordinator();
            session.setAttribute("currentUser", coordinator);
            return "redirect:/dashboard";
        }
        
        // Check for predefined controller credentials
        if (username.equals("exam_controller") && password.equals("controller123")) {
            User controller = userService.findOrCreateController();
            session.setAttribute("currentUser", controller);
            return "redirect:/dashboard";
        }
        
        // For student and teacher login
        User user = userService.authenticate(username, password);
        if (user != null) {
            // Design Pattern: State - Checking account status
            if (user.getStatus() == AccountStatus.PENDING) {
                model.addAttribute("error", "Your account is still pending approval. Please check back after 24 hours.");
                return "auth/login";
            } else if (user.getStatus() == AccountStatus.REJECTED) {
                model.addAttribute("error", "Your account registration has been rejected. Please contact administration.");
                return "auth/login";
            }
            
            session.setAttribute("currentUser", user);
            return "redirect:/dashboard";
        } else {
            model.addAttribute("error", "Invalid username or password");
            return "auth/login";
        }
    }

    // Architecture Pattern: MVC - Controller endpoint
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
} 