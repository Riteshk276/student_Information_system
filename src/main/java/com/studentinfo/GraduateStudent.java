package com.studentinfo;

public class GraduateStudent extends Student {
    private String researchArea;
    private String supervisor;
    private boolean isThesisSubmitted;
    private static int totalGradStudents = 0;
    
    // Default constructor
    public GraduateStudent() {
        super();
        this.researchArea = "Undecided";
        this.supervisor = "Not Assigned";
        this.isThesisSubmitted = false;
        totalGradStudents++;
    }
    
    // Parameterized constructor
    public GraduateStudent(String name, int age, String address, 
                          String studentId, String department,
                          String researchArea, String supervisor) {
        super(name, age, address, studentId, department);
        this.researchArea = researchArea;
        this.supervisor = supervisor;
        this.isThesisSubmitted = false;
        totalGradStudents++;
    }
    
    // Constructor overloading
    public GraduateStudent(String name, int age, String address, 
                          String studentId, String department,
                          String researchArea) {
        this(name, age, address, studentId, department, researchArea, "Pending Assignment");
    }
    
    // Getters and Setters
    public String getResearchArea() {
        return researchArea;
    }
    
    public void setResearchArea(String researchArea) {
        this.researchArea = researchArea;
    }
    
    public String getSupervisor() {
        return supervisor;
    }
    
    public void setSupervisor(String supervisor) {
        this.supervisor = supervisor;
    }
    
    public boolean isThesisSubmitted() {
        return isThesisSubmitted;
    }
    
    public void setThesisSubmitted(boolean thesisSubmitted) {
        isThesisSubmitted = thesisSubmitted;
    }
    
    // Static method to get total number of graduate students
    public static int getTotalGradStudents() {
        return totalGradStudents;
    }
    
    // Override display method from Student
    @Override
    public void displayInfo() {
        super.displayInfo();
        System.out.println("Research Area: " + researchArea);
        System.out.println("Supervisor: " + supervisor);
        System.out.println("Thesis Status: " + (isThesisSubmitted ? "Submitted" : "Not Submitted"));
    }
    
    // Override calculateGrade method to have different grading scale for graduate students
    @Override
    public char calculateGrade(double marks) {
        if (marks >= 90) return 'A';
        else if (marks >= 80) return 'B';
        else if (marks >= 70) return 'C';
        else return 'F'; // Graduate students need at least 70 to pass
    }
    
    @Override
    public boolean hasPassed(double marks) {
        return marks >= 70; // Graduate students need at least 70 to pass
    }
    
    // Method specific to graduate students
    public void submitThesis() {
        this.isThesisSubmitted = true;
        System.out.println("Thesis submitted successfully for " + getName());
    }
} 