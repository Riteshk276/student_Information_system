package com.studentinfo.gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import com.studentinfo.*;
import com.studentinfo.db.StudentRepository;
import com.studentinfo.db.CourseRepository;

public class GradePanel extends JPanel {
    private JTable gradeTable;
    private DefaultTableModel tableModel;
    private JComboBox<String> studentCombo;
    private JComboBox<String> courseCombo;
    private JTextField marksField;
    
    // MongoDB repositories
    private StudentRepository studentRepository;
    private CourseRepository courseRepository;
    
    public GradePanel() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Initialize repositories
        studentRepository = new StudentRepository();
        courseRepository = new CourseRepository();
        
        // Top panel for adding grades
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createTitledBorder("Add/Update Grade"));
        
        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Grade fields
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Student:"), gbc);
        gbc.gridx = 1;
        studentCombo = new JComboBox<>();
        updateStudentCombo();
        formPanel.add(studentCombo, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Course:"), gbc);
        gbc.gridx = 1;
        courseCombo = new JComboBox<>();
        updateCourseCombo();
        formPanel.add(courseCombo, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Marks:"), gbc);
        gbc.gridx = 1;
        marksField = new JTextField(5);
        formPanel.add(marksField, gbc);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton addButton = new JButton("Add/Update Grade");
        JButton refreshButton = new JButton("Refresh");
        
        addButton.addActionListener(e -> addGrade());
        refreshButton.addActionListener(e -> {
            updateStudentCombo();
            updateCourseCombo();
            updateGradeTable();
        });
        
        buttonPanel.add(refreshButton);
        buttonPanel.add(addButton);
        
        // Add components to top panel
        topPanel.add(formPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Create table for displaying grades
        String[] columnNames = {"Student ID", "Student Name", "Course ID", "Course Name", "Marks", "Grade", "Status"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table cells non-editable
            }
        };
        
        gradeTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(gradeTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Grades"));
        
        // Stats panel
        JPanel statsPanel = new JPanel(new GridLayout(2, 1));
        statsPanel.setBorder(BorderFactory.createTitledBorder("Statistics"));
        
        // GPA Calculator
        JPanel gpaPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel studentLabel = new JLabel("Calculate GPA for:");
        JComboBox<String> studentGpaCombo = new JComboBox<>();
        updateStudentCombo(studentGpaCombo);
        JButton calculateButton = new JButton("Calculate GPA");
        JLabel gpaLabel = new JLabel("GPA: ");
        
        calculateButton.addActionListener(e -> {
            int selectedIndex = studentGpaCombo.getSelectedIndex();
            if (selectedIndex >= 0 && selectedIndex < StudentPanel.getStudents().size()) {
                Student student = StudentPanel.getStudents().get(selectedIndex);
                calculateAndDisplayGPA(student, gpaLabel);
            } else {
                JOptionPane.showMessageDialog(this, "Please select a student.", 
                                            "Selection Required", JOptionPane.WARNING_MESSAGE);
            }
        });
        
        gpaPanel.add(studentLabel);
        gpaPanel.add(studentGpaCombo);
        gpaPanel.add(calculateButton);
        gpaPanel.add(gpaLabel);
        
        // Grade Distribution
        JPanel distributionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton distributionButton = new JButton("Show Grade Distribution");
        
        distributionButton.addActionListener(e -> showGradeDistribution());
        
        distributionPanel.add(distributionButton);
        
        statsPanel.add(gpaPanel);
        statsPanel.add(distributionPanel);
        
        // Add all components to main panel
        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(statsPanel, BorderLayout.SOUTH);
        
        // Initialize with data from MongoDB
        updateGradeTable();
    }
    
    private void updateStudentCombo() {
        updateStudentCombo(studentCombo);
    }
    
    private void updateStudentCombo(JComboBox<String> combo) {
        combo.removeAllItems();
        List<Student> students = StudentPanel.getStudents();
        for (Student student : students) {
            combo.addItem(student.getStudentId() + " - " + student.getName());
        }
    }
    
    private void updateCourseCombo() {
        courseCombo.removeAllItems();
        List<Course> courses = CoursePanel.getCourses();
        for (Course course : courses) {
            courseCombo.addItem(course.getCourseId() + " - " + course.getCourseName());
        }
    }
    
    private void addGrade() {
        // Validate input fields
        if (studentCombo.getSelectedIndex() < 0 || courseCombo.getSelectedIndex() < 0 || 
            marksField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select a student, course and enter marks.", 
                                        "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        double marks;
        try {
            marks = Double.parseDouble(marksField.getText());
            if (marks < 0 || marks > 100) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter valid marks (0-100).", 
                                        "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Get selected student and course
        Student student = StudentPanel.getStudents().get(studentCombo.getSelectedIndex());
        Course course = CoursePanel.getCourses().get(courseCombo.getSelectedIndex());
        
        // Check if student is already enrolled in this course
        boolean enrolled = false;
        for (Course c : student.getEnrolledCourses()) {
            if (c.getCourseId().equals(course.getCourseId())) {
                enrolled = true;
                break;
            }
        }
        
        // Update the student object
        if (!enrolled) {
            student.enrollCourse(course, marks);
        } else {
            student.addMarks(course.getCourseId(), marks);
        }
        
        // Update in MongoDB database
        boolean success = studentRepository.updateStudentGrade(student.getStudentId(), course.getCourseId(), marks);
        
        if (success) {
            // Update the grade table
            updateGradeTable();
            
            // Clear marks field
            marksField.setText("");
            
            JOptionPane.showMessageDialog(this, "Grade added/updated successfully!", 
                                        "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Error updating grade in database.", 
                                        "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void updateGradeTable() {
        // Clear existing rows
        tableModel.setRowCount(0);
        
        // Get all students
        List<Student> students = StudentPanel.getStudents();
        
        // For each student, add their courses and grades to the table
        for (Student student : students) {
            List<Course> courses = student.getEnrolledCourses();
            
            for (Course course : courses) {
                double marks = student.getCourseMarks().get(course.getCourseId());
                char grade = student.calculateGrade(marks);
                boolean passed = student.hasPassed(marks);
                
                Object[] rowData = {
                    student.getStudentId(),
                    student.getName(),
                    course.getCourseId(),
                    course.getCourseName(),
                    marks,
                    grade,
                    passed ? "Passed" : "Failed"
                };
                
                tableModel.addRow(rowData);
            }
        }
    }
    
    private void calculateAndDisplayGPA(Student student, JLabel gpaLabel) {
        // Extract grades from the student's courses
        List<Course> courses = student.getEnrolledCourses();
        char[] grades = new char[courses.size()];
        
        int i = 0;
        for (Course course : courses) {
            double marks = student.getCourseMarks().get(course.getCourseId());
            grades[i++] = student.calculateGrade(marks);
        }
        
        // Calculate GPA
        double gpa = 0.0;
        if (grades.length > 0) {
            gpa = student.calculateGPA(grades);
        }
        
        // Display the GPA
        gpaLabel.setText("GPA: " + String.format("%.2f", gpa));
    }
    
    private void showGradeDistribution() {
        // Count distribution of grades
        int countA = 0, countB = 0, countC = 0, countD = 0, countF = 0;
        
        for (int row = 0; row < tableModel.getRowCount(); row++) {
            char grade = ((String) tableModel.getValueAt(row, 5)).charAt(0);
            switch (grade) {
                case 'A': countA++; break;
                case 'B': countB++; break;
                case 'C': countC++; break;
                case 'D': countD++; break;
                case 'F': countF++; break;
            }
        }
        
        // Display in a dialog
        JOptionPane.showMessageDialog(this,
            "Grade Distribution:\n\n" +
            "A: " + countA + "\n" +
            "B: " + countB + "\n" +
            "C: " + countC + "\n" +
            "D: " + countD + "\n" +
            "F: " + countF,
            "Grade Distribution",
            JOptionPane.INFORMATION_MESSAGE);
    }
} 