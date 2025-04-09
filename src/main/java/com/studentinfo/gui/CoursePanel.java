package com.studentinfo.gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import com.studentinfo.*;
import com.studentinfo.db.CourseRepository;

public class CoursePanel extends JPanel {
    private JTable courseTable;
    private DefaultTableModel tableModel;
    private JTextField courseIdField, courseNameField, instructorField;
    private JSpinner creditsSpinner;
    
    // Store courses for the demo
    private static List<Course> courses = new ArrayList<>();
    // MongoDB repository
    private CourseRepository courseRepository;
    
    public CoursePanel() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Initialize repository
        courseRepository = new CourseRepository();
        
        // Top panel for adding courses
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createTitledBorder("Add New Course"));
        
        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Course fields
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Course ID:"), gbc);
        gbc.gridx = 1;
        courseIdField = new JTextField(10);
        formPanel.add(courseIdField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Course Name:"), gbc);
        gbc.gridx = 1;
        courseNameField = new JTextField(20);
        formPanel.add(courseNameField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Instructor:"), gbc);
        gbc.gridx = 1;
        instructorField = new JTextField(20);
        formPanel.add(instructorField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Credits:"), gbc);
        gbc.gridx = 1;
        SpinnerNumberModel spinnerModel = new SpinnerNumberModel(3, 1, 6, 1);
        creditsSpinner = new JSpinner(spinnerModel);
        formPanel.add(creditsSpinner, gbc);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton addButton = new JButton("Add Course");
        JButton clearButton = new JButton("Clear");
        JButton refreshButton = new JButton("Refresh");
        
        addButton.addActionListener(e -> addCourse());
        clearButton.addActionListener(e -> clearFields());
        refreshButton.addActionListener(e -> loadCoursesFromDB());
        
        buttonPanel.add(refreshButton);
        buttonPanel.add(addButton);
        buttonPanel.add(clearButton);
        
        // Add components to top panel
        topPanel.add(formPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Create table for displaying courses
        String[] columnNames = {"Course ID", "Course Name", "Instructor", "Credits"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table cells non-editable
            }
        };
        
        courseTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(courseTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Courses"));
        
        // Bottom panel with action buttons
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton editButton = new JButton("Edit");
        JButton deleteButton = new JButton("Delete");
        
        editButton.addActionListener(e -> editSelectedCourse());
        deleteButton.addActionListener(e -> deleteSelectedCourse());
        
        bottomPanel.add(editButton);
        bottomPanel.add(deleteButton);
        
        // Add all components to main panel
        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
        
        // Load courses from database
        loadCoursesFromDB();
        
        // If no courses in DB, add demo data
        if (courses.isEmpty()) {
            addDemoData();
        }
    }
    
    private void loadCoursesFromDB() {
        try {
            // Clear existing data
            courses.clear();
            tableModel.setRowCount(0);
            
            // Load from MongoDB
            List<Course> loadedCourses = courseRepository.getAllCourses();
            
            // Add to our list and table
            for (Course course : loadedCourses) {
                courses.add(course);
                addCourseToTable(course);
            }
            
            System.out.println("Loaded " + courses.size() + " courses from database");
        } catch (Exception e) {
            System.err.println("Error loading courses from database: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading courses from database. Check console for details.",
                                         "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void addDemoData() {
        // Add some demo courses to the list, table, and database
        Course course1 = new Course("CS101", "Java Programming", "Prof. Smith", 4);
        courses.add(course1);
        addCourseToTable(course1);
        courseRepository.saveCourse(course1);
        
        Course course2 = new Course("CS102", "Database Systems", "Prof. Johnson", 3);
        courses.add(course2);
        addCourseToTable(course2);
        courseRepository.saveCourse(course2);
        
        Course course3 = new Course("CS103", "Algorithms", "Prof. Williams", 4);
        courses.add(course3);
        addCourseToTable(course3);
        courseRepository.saveCourse(course3);
    }
    
    private void addCourseToTable(Course course) {
        Object[] rowData = {
            course.getCourseId(),
            course.getCourseName(),
            course.getInstructorName(),
            course.getCredits()
        };
        
        tableModel.addRow(rowData);
    }
    
    private void addCourse() {
        // Validate input fields
        if (courseIdField.getText().isEmpty() || courseNameField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Course ID and Course Name are required fields.", 
                                        "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Create course object
        Course course = new Course(
            courseIdField.getText(),
            courseNameField.getText(),
            instructorField.getText(),
            (int) creditsSpinner.getValue()
        );
        
        // Save to MongoDB first
        boolean success = courseRepository.saveCourse(course);
        
        if (success) {
            // Add course to list and table
            courses.add(course);
            addCourseToTable(course);
            
            // Clear fields after adding
            clearFields();
            
            JOptionPane.showMessageDialog(this, "Course added successfully!", 
                                        "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Error saving course to database.", 
                                        "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void editSelectedCourse() {
        int selectedRow = courseTable.getSelectedRow();
        if (selectedRow >= 0) {
            // Load course data into form for editing
            Course course = courses.get(selectedRow);
            
            courseIdField.setText(course.getCourseId());
            courseNameField.setText(course.getCourseName());
            instructorField.setText(course.getInstructorName());
            creditsSpinner.setValue(course.getCredits());
            
            // Disable course ID field as we're editing
            courseIdField.setEditable(false);
            
            JOptionPane.showMessageDialog(this, 
                "Course data loaded for editing. Make your changes and click 'Add Course' to update.", 
                "Edit Course", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Please select a course to edit.", 
                                        "Selection Required", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    private void deleteSelectedCourse() {
        int selectedRow = courseTable.getSelectedRow();
        if (selectedRow >= 0) {
            Course course = courses.get(selectedRow);
            String courseId = course.getCourseId();
            
            int response = JOptionPane.showConfirmDialog(this, 
                "Are you sure you want to delete this course?", 
                "Confirm Deletion", JOptionPane.YES_NO_OPTION);
            
            if (response == JOptionPane.YES_OPTION) {
                // Delete from MongoDB first
                boolean success = courseRepository.deleteCourse(courseId);
                
                if (success) {
                    // Then remove from list and table
                    courses.remove(selectedRow);
                    tableModel.removeRow(selectedRow);
                    JOptionPane.showMessageDialog(this, "Course deleted successfully!", 
                                                "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Error deleting course from database.", 
                                                "Database Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a course to delete.", 
                                        "Selection Required", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    private void clearFields() {
        courseIdField.setText("");
        courseNameField.setText("");
        instructorField.setText("");
        creditsSpinner.setValue(3);
        courseIdField.setEditable(true); // Re-enable course ID field
    }
    
    // Static method to get courses list (for other panels)
    public static List<Course> getCourses() {
        return courses;
    }
} 