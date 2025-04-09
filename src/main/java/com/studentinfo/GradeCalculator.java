package com.studentinfo;

public interface GradeCalculator {
    // Calculate grade based on marks
    char calculateGrade(double marks);
    
    // Calculate GPA based on grades
    double calculateGPA(char[] grades);
    
    // Check if student has passed
    boolean hasPassed(double marks);
} 