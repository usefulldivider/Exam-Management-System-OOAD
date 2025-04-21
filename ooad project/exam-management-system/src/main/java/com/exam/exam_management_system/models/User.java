// Architecture Pattern: MVC - Model
// Design Pattern: Template Method - Abstract base class for user types
// Design Pattern: State - Uses AccountStatus for user state management

package com.exam.exam_management_system.models;
import com.exam.exam_management_system.models.enums.UserRole;
import com.exam.exam_management_system.models.enums.AccountStatus;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

// Architecture Pattern: MVC - Model
// Design Pattern: Template Method - Abstract base class
@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class User {
    // Architecture Pattern: MVC - Model attributes
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Design Pattern: State - Role state
    @Enumerated(EnumType.STRING)
    private UserRole role;

    // Architecture Pattern: MVC - Model attributes
    @Column(unique = true)
    private String username;
    private String password;
    private String name;
    private String email;
    
    // Design Pattern: State - Account status state
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountStatus status = AccountStatus.PENDING;

    // Architecture Pattern: MVC - Model attributes
    @CreationTimestamp
    @Column(updatable = false, nullable = false)
    private LocalDateTime createdAt;
    
    // Design Pattern: Template Method - Default constructor
    public User() {
    }
    
    // Design Pattern: Template Method - Parameterized constructor
    public User(String username, String password, String name, String email, UserRole role) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.email = email;
        this.role = role;
        this.status = AccountStatus.PENDING; // createdAt will be set automatically
    }
    
    // Architecture Pattern: MVC - Model getters and setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    // Design Pattern: State - Role getter and setter
    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    // Design Pattern: State - Status getter and setter
    public AccountStatus getStatus() {
        return status;
    }

    public void setStatus(AccountStatus status) {
        this.status = status;
    }

    // Architecture Pattern: MVC - Model getter and setter
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}