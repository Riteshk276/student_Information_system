package com.studentinfo;

public class Main {
    public static void main(String[] args) {
        System.out.println("Student Information System");
        System.out.println("==========================");
        
        // Create courses
        Course java = new Course("CS101", "Java Programming", "Prof. Smith", 4);
        Course database = new Course("CS102", "Database Systems", 3);
        Course algorithms = new Course("CS103", "Algorithms", "Prof. Johnson", 4);
        
        // Create undergraduate student
        Student student1 = new Student("John Doe", 20, "123 College St", "S1001", "Computer Science");
        student1.enrollCourse(java);
        student1.enrollCourse(database, 85.0);
        student1.addMarks("CS101", 78.5);
        
        // Create graduate student
        GraduateStudent gradStudent = new GraduateStudent(
            "Jane Smith", 25, "456 University Ave",
            "G2001", "Computer Science",
            "Machine Learning", "Prof. Williams"
        );
        gradStudent.enrollCourse(java, 92.0);
        gradStudent.enrollCourse(algorithms, 88.5);
        
        // Display information and demonstrate functionality
        System.out.println("\nUndergraduate Student Information:");
        student1.displayInfo();
        System.out.println("\nCourses and Grades:");
        student1.displayGrades();
        
        System.out.println("\nGraduate Student Information:");
        gradStudent.displayInfo();
        System.out.println("\nCourses and Grades:");
        gradStudent.displayGrades();
        
        // Demonstrate thesis submission
        gradStudent.submitThesis();
        
        // Demonstrate static method usage
        System.out.println("\nStatistics:");
        System.out.println("Total Courses: " + Course.getTotalCourses());
        System.out.println("Total Students: " + Student.getTotalStudents());
        System.out.println("Total Graduate Students: " + GraduateStudent.getTotalGradStudents());
        
        // Demonstrate grade calculation for different marks
        System.out.println("\nGrade Calculation Examples:");
        double[] testMarks = {95.0, 85.0, 75.0, 65.0, 55.0};
        
        System.out.println("Undergraduate Grading:");
        for (double mark : testMarks) {
            char grade = student1.calculateGrade(mark);
            System.out.println("Mark: " + mark + ", Grade: " + grade + 
                              ", Passed: " + student1.hasPassed(mark));
        }
        
        System.out.println("\nGraduate Grading:");
        for (double mark : testMarks) {
            char grade = gradStudent.calculateGrade(mark);
            System.out.println("Mark: " + mark + ", Grade: " + grade + 
                              ", Passed: " + gradStudent.hasPassed(mark));
        }
    }
} 