package com.exam.exam_management_system.models.enums;

public enum MCQOption {
    A, B, C, D;

    public static boolean isValidOption(String option) {
        try {
            MCQOption.valueOf(option.toUpperCase());
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
} 