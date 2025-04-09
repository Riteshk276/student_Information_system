package com.studentinfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Student extends Person implements GradeCalculator {
    private String studentId;
    private String department;
    private List<Course> enrolledCourses;
    private Map<String, Double> courseMarks;
    private static int totalStudents = 0;
    
    // Default constructor
    public Student() {
        super();
        this.studentId = "S000";
        this.department = "Undecided";
        this.enrolledCourses = new ArrayList<>();
        this.courseMarks = new HashMap<>();
        totalStudents++;
    }
    
    // Parameterized constructor
    public Student(String name, int age, String address, String studentId, String department) {
        super(name, age, address);
        this.studentId = studentId;
        this.department = department;
        this.enrolledCourses = new ArrayList<>();
        this.courseMarks = new HashMap<>();
        totalStudents++;
    }
    
    // Method overloading
    public void enrollCourse(Course course) {
        enrolledCourses.add(course);
        courseMarks.put(course.getCourseId(), 0.0);
    }
    
    public void enrollCourse(Course course, double initialMarks) {
        enrolledCourses.add(course);
        courseMarks.put(course.getCourseId(), initialMarks);
    }
    
    // Add marks for a course
    public void addMarks(String courseId, double marks) {
        if (courseMarks.containsKey(courseId)) {
            courseMarks.put(courseId, marks);
        } else {
            System.out.println("Student not enrolled in this course.");
        }
    }
    
    // Static method to get total number of students
    public static int getTotalStudents() {
        return totalStudents;
    }
    
    // Getters and Setters
    public String getStudentId() {
        return studentId;
    }
    
    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }
    
    public String getDepartment() {
        return department;
    }
    
    public void setDepartment(String department) {
        this.department = department;
    }
    
    public List<Course> getEnrolledCourses() {
        return enrolledCourses;
    }
    
    public Map<String, Double> getCourseMarks() {
        return courseMarks;
    }
    
    // Override display method from Person
    @Override
    public void displayInfo() {
        super.displayInfo();
        System.out.println("Student ID: " + studentId);
        System.out.println("Department: " + department);
        System.out.println("Enrolled Courses: " + enrolledCourses.size());
    }
    
    // Implementing GradeCalculator methods
    @Override
    public char calculateGrade(double marks) {
        if (marks >= 90) return 'A';
        else if (marks >= 80) return 'B';
        else if (marks >= 70) return 'C';
        else if (marks >= 60) return 'D';
        else return 'F';
    }
    
    @Override
    public double calculateGPA(char[] grades) {
        double total = 0.0;
        for (char grade : grades) {
            switch (grade) {
                case 'A': total += 4.0; break;
                case 'B': total += 3.0; break;
                case 'C': total += 2.0; break;
                case 'D': total += 1.0; break;
                case 'F': total += 0.0; break;
            }
        }
        return total / grades.length;
    }
    
    @Override
    public boolean hasPassed(double marks) {
        return marks >= 60;
    }
    
    // Display student's course grades
    public void displayGrades() {
        System.out.println("Grades for student: " + getName() + " (ID: " + studentId + ")");
        for (Course course : enrolledCourses) {
            double marks = courseMarks.get(course.getCourseId());
            char grade = calculateGrade(marks);
            System.out.println(course.getCourseName() + ": " + marks + " (" + grade + ")");
        }
    }
} 