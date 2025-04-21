package com.exam.exam_management_system.models.enums;

/**
 * Represents the different states an exam can be in.
 */
public enum ExamState {
    DRAFT,          // Initial state, being created by teacher
    PROOFREAD,      // Submitted for proofreading by another teacher
    STORED_IN_COE,  // Exam paper is stored with Controller of Examination
    ACTIVE,         // Exam is can be taken
    COMPLETED,      // Exam duration finished
    EVALUATED,      // Answers graded, marks calculated
    RESULTS_PUBLISHED // Final results visible to students
}
