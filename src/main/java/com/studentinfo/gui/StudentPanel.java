package com.studentinfo.gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import com.studentinfo.*;
import com.studentinfo.db.StudentRepository;

public class StudentPanel extends JPanel {
    private JTable studentTable;
    private DefaultTableModel tableModel;
    private JTextField nameField, ageField, addressField, studentIdField, departmentField;
    private JComboBox<String> studentTypeCombo;
    private JTextField researchAreaField, supervisorField;
    private JPanel gradFieldsPanel;
    
    // Store students for the demo
    private static List<Student> students = new ArrayList<>();
    // MongoDB repository
    private StudentRepository studentRepository;
    
    public StudentPanel() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Initialize repository
        studentRepository = new StudentRepository();
        
        // Top panel for adding students
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createTitledBorder("Add New Student"));
        
        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Basic info
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Student Type:"), gbc);
        gbc.gridx = 1;
        studentTypeCombo = new JComboBox<>(new String[]{"Undergraduate", "Graduate"});
        formPanel.add(studentTypeCombo, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        nameField = new JTextField(20);
        formPanel.add(nameField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Age:"), gbc);
        gbc.gridx = 1;
        ageField = new JTextField(5);
        formPanel.add(ageField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Address:"), gbc);
        gbc.gridx = 1;
        addressField = new JTextField(20);
        formPanel.add(addressField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(new JLabel("Student ID:"), gbc);
        gbc.gridx = 1;
        studentIdField = new JTextField(10);
        formPanel.add(studentIdField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 5;
        formPanel.add(new JLabel("Department:"), gbc);
        gbc.gridx = 1;
        departmentField = new JTextField(15);
        formPanel.add(departmentField, gbc);
        
        // Graduate student fields (initially hidden)
        gradFieldsPanel = new JPanel(new GridBagLayout());
        gradFieldsPanel.setBorder(BorderFactory.createTitledBorder("Graduate Student Info"));
        
        GridBagConstraints gbc2 = new GridBagConstraints();
        gbc2.insets = new Insets(5, 5, 5, 5);
        gbc2.anchor = GridBagConstraints.WEST;
        
        gbc2.gridx = 0; gbc2.gridy = 0;
        gradFieldsPanel.add(new JLabel("Research Area:"), gbc2);
        gbc2.gridx = 1;
        researchAreaField = new JTextField(15);
        gradFieldsPanel.add(researchAreaField, gbc2);
        
        gbc2.gridx = 0; gbc2.gridy = 1;
        gradFieldsPanel.add(new JLabel("Supervisor:"), gbc2);
        gbc2.gridx = 1;
        supervisorField = new JTextField(15);
        gradFieldsPanel.add(supervisorField, gbc2);
        
        gradFieldsPanel.setVisible(false);
        
        // Add action listener to student type combo
        studentTypeCombo.addActionListener(e -> {
            int selectedIndex = studentTypeCombo.getSelectedIndex();
            gradFieldsPanel.setVisible(selectedIndex == 1); // Show graduate fields only when Graduate is selected
            revalidate();
            repaint();
        });
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton addButton = new JButton("Add Student");
        JButton clearButton = new JButton("Clear");
        JButton refreshButton = new JButton("Refresh");
        
        addButton.addActionListener(e -> addStudent());
        clearButton.addActionListener(e -> clearFields());
        refreshButton.addActionListener(e -> loadStudentsFromDB());
        
        buttonPanel.add(refreshButton);
        buttonPanel.add(addButton);
        buttonPanel.add(clearButton);
        
        // Add components to top panel
        topPanel.add(formPanel, BorderLayout.CENTER);
        topPanel.add(gradFieldsPanel, BorderLayout.SOUTH);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Create table for displaying students
        String[] columnNames = {"Type", "Student ID", "Name", "Age", "Department", "Research Area", "Supervisor"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table cells non-editable
            }
        };
        
        studentTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(studentTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Students"));
        
        // Bottom panel with action buttons
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton editButton = new JButton("Edit");
        JButton deleteButton = new JButton("Delete");
        
        editButton.addActionListener(e -> editSelectedStudent());
        deleteButton.addActionListener(e -> deleteSelectedStudent());
        
        bottomPanel.add(editButton);
        bottomPanel.add(deleteButton);
        
        // Add all components to main panel
        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
        
        // Load students from database
        loadStudentsFromDB();
        
        // If no students in DB, add demo data
        if (students.isEmpty()) {
            addDemoData();
        }
    }
    
    private void loadStudentsFromDB() {
        try {
            // Clear existing data
            students.clear();
            tableModel.setRowCount(0);
            
            // Load from MongoDB
            List<Student> loadedStudents = studentRepository.getAllStudents();
            
            // Add to our list and table
            for (Student student : loadedStudents) {
                students.add(student);
                addStudentToTable(student);
            }
            
            System.out.println("Loaded " + students.size() + " students from database");
        } catch (Exception e) {
            System.err.println("Error loading students from database: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading students from database. Check console for details.",
                                         "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void addDemoData() {
        // Add some demo students to the list, table, and database
        Student undergrad = new Student("John Doe", 20, "123 College St", "S1001", "Computer Science");
        students.add(undergrad);
        addStudentToTable(undergrad);
        studentRepository.saveStudent(undergrad);
        
        GraduateStudent grad = new GraduateStudent("Jane Smith", 25, "456 University Ave", 
                                                "G2001", "Computer Science", 
                                                "Machine Learning", "Prof. Williams");
        students.add(grad);
        addStudentToTable(grad);
        studentRepository.saveStudent(grad);
    }
    
    private void addStudentToTable(Student student) {
        Object[] rowData;
        
        if (student instanceof GraduateStudent) {
            GraduateStudent grad = (GraduateStudent) student;
            rowData = new Object[]{
                "Graduate",
                grad.getStudentId(),
                grad.getName(),
                grad.getAge(),
                grad.getDepartment(),
                grad.getResearchArea(),
                grad.getSupervisor()
            };
        } else {
            rowData = new Object[]{
                "Undergraduate",
                student.getStudentId(),
                student.getName(),
                student.getAge(),
                student.getDepartment(),
                "-",
                "-"
            };
        }
        
        tableModel.addRow(rowData);
    }
    
    private void addStudent() {
        // Validate input fields
        if (nameField.getText().isEmpty() || ageField.getText().isEmpty() || 
            addressField.getText().isEmpty() || studentIdField.getText().isEmpty() || 
            departmentField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all required fields.", 
                                        "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int age;
        try {
            age = Integer.parseInt(ageField.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number for age.", 
                                        "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Create student object
        Student student;
        if (studentTypeCombo.getSelectedIndex() == 1) {
            // Graduate student
            if (researchAreaField.getText().isEmpty() || supervisorField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all graduate student fields.", 
                                            "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            student = new GraduateStudent(
                nameField.getText(),
                age,
                addressField.getText(),
                studentIdField.getText(),
                departmentField.getText(),
                researchAreaField.getText(),
                supervisorField.getText()
            );
        } else {
            // Undergraduate student
            student = new Student(
                nameField.getText(),
                age,
                addressField.getText(),
                studentIdField.getText(),
                departmentField.getText()
            );
        }
        
        // Save to MongoDB first
        boolean success = studentRepository.saveStudent(student);
        
        if (success) {
            // Add student to list and table
            students.add(student);
            addStudentToTable(student);
            
            // Clear fields after adding
            clearFields();
            
            JOptionPane.showMessageDialog(this, "Student added successfully!", 
                                       "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Error saving student to database.", 
                                       "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void editSelectedStudent() {
        int selectedRow = studentTable.getSelectedRow();
        if (selectedRow >= 0) {
            // For simplicity, we'll just load the student data into the form for editing
            Student student = students.get(selectedRow);
            
            // Set form fields
            studentIdField.setText(student.getStudentId());
            nameField.setText(student.getName());
            ageField.setText(String.valueOf(student.getAge()));
            addressField.setText(student.getAddress());
            departmentField.setText(student.getDepartment());
            
            if (student instanceof GraduateStudent) {
                GraduateStudent grad = (GraduateStudent) student;
                studentTypeCombo.setSelectedIndex(1);
                researchAreaField.setText(grad.getResearchArea());
                supervisorField.setText(grad.getSupervisor());
            } else {
                studentTypeCombo.setSelectedIndex(0);
            }
            
            // Disable student ID field as we're editing
            studentIdField.setEditable(false);
            
            JOptionPane.showMessageDialog(this, 
                "Student data loaded for editing. Make your changes and click 'Add Student' to update.", 
                "Edit Student", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Please select a student to edit.", 
                                        "Selection Required", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    private void deleteSelectedStudent() {
        int selectedRow = studentTable.getSelectedRow();
        if (selectedRow >= 0) {
            Student student = students.get(selectedRow);
            String studentId = student.getStudentId();
            
            int response = JOptionPane.showConfirmDialog(this, 
                "Are you sure you want to delete this student?", 
                "Confirm Deletion", JOptionPane.YES_NO_OPTION);
            
            if (response == JOptionPane.YES_OPTION) {
                // Delete from MongoDB first
                boolean success = studentRepository.deleteStudent(studentId);
                
                if (success) {
                    // Then remove from list and table
                    students.remove(selectedRow);
                    tableModel.removeRow(selectedRow);
                    JOptionPane.showMessageDialog(this, "Student deleted successfully!", 
                                               "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Error deleting student from database.", 
                                               "Database Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a student to delete.", 
                                        "Selection Required", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    private void clearFields() {
        nameField.setText("");
        ageField.setText("");
        addressField.setText("");
        studentIdField.setText("");
        departmentField.setText("");
        researchAreaField.setText("");
        supervisorField.setText("");
        studentTypeCombo.setSelectedIndex(0);
        gradFieldsPanel.setVisible(false);
        studentIdField.setEditable(true); // Re-enable student ID field
    }
    
    // Static method to get students list (for other panels)
    public static List<Student> getStudents() {
        return students;
    }
} 