package com.studentinfo;

public class Course {
    private String courseId;
    private String courseName;
    private String instructorName;
    private int credits;
    private static int totalCourses = 0;
    
    // Default constructor
    public Course() {
        this.courseId = "C000";
        this.courseName = "Unknown";
        this.instructorName = "Unknown";
        this.credits = 0;
        totalCourses++;
    }
    
    // Parameterized constructor
    public Course(String courseId, String courseName, String instructorName, int credits) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.instructorName = instructorName;
        this.credits = credits;
        totalCourses++;
    }
    
    // Constructor overloading
    public Course(String courseId, String courseName, int credits) {
        this(courseId, courseName, "Not Assigned", credits);
    }
    
    // Getters and Setters
    public String getCourseId() {
        return courseId;
    }
    
    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }
    
    public String getCourseName() {
        return courseName;
    }
    
    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }
    
    public String getInstructorName() {
        return instructorName;
    }
    
    public void setInstructorName(String instructorName) {
        this.instructorName = instructorName;
    }
    
    public int getCredits() {
        return credits;
    }
    
    public void setCredits(int credits) {
        this.credits = credits;
    }
    
    // Static method to get total number of courses
    public static int getTotalCourses() {
        return totalCourses;
    }
    
    // Display course information
    public void displayCourseInfo() {
        System.out.println("Course ID: " + courseId);
        System.out.println("Course Name: " + courseName);
        System.out.println("Instructor: " + instructorName);
        System.out.println("Credits: " + credits);
    }
} 