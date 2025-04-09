package com.studentinfo.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import com.studentinfo.*;

public class MainFrame extends JFrame {
    private JPanel mainPanel;
    private JTabbedPane tabbedPane;

    public MainFrame() {
        // Set up the main frame
        setTitle("Student Information System");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create main panel with BorderLayout
        mainPanel = new JPanel(new BorderLayout());
        
        // Create menu bar
        createMenuBar();
        
        // Create tabbed pane for different sections
        tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Dashboard", createDashboardPanel());
        tabbedPane.addTab("Students", new StudentPanel());
        tabbedPane.addTab("Courses", new CoursePanel());
        tabbedPane.addTab("Grades", new GradePanel());
        
        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        
        // Add status bar
        JPanel statusBar = new JPanel(new BorderLayout());
        statusBar.setBorder(BorderFactory.createEtchedBorder());
        JLabel statusLabel = new JLabel(" Ready");
        statusBar.add(statusLabel, BorderLayout.WEST);
        mainPanel.add(statusBar, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        
        // File menu
        JMenu fileMenu = new JMenu("File");
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(e -> System.exit(0));
        fileMenu.add(exitItem);
        
        // Edit menu
        JMenu editMenu = new JMenu("Edit");
        JMenuItem prefItem = new JMenuItem("Preferences");
        editMenu.add(prefItem);
        
        // Help menu
        JMenu helpMenu = new JMenu("Help");
        JMenuItem aboutItem = new JMenuItem("About");
        aboutItem.addActionListener(e -> {
            JOptionPane.showMessageDialog(this,
                "Student Information System\nVersion 1.0\n© 2023",
                "About", JOptionPane.INFORMATION_MESSAGE);
        });
        helpMenu.add(aboutItem);
        
        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(helpMenu);
        
        setJMenuBar(menuBar);
    }
    
    private JPanel createDashboardPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Welcome message
        JLabel welcomeLabel = new JLabel("Welcome to Student Information System");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        panel.add(welcomeLabel, BorderLayout.NORTH);
        
        // Stats panel
        JPanel statsPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        statsPanel.setBorder(BorderFactory.createTitledBorder("Statistics"));
        
        statsPanel.add(new JLabel("Total Students:"));
        statsPanel.add(new JLabel(String.valueOf(Student.getTotalStudents())));
        
        statsPanel.add(new JLabel("Total Graduate Students:"));
        statsPanel.add(new JLabel(String.valueOf(GraduateStudent.getTotalGradStudents())));
        
        statsPanel.add(new JLabel("Total Courses:"));
        statsPanel.add(new JLabel(String.valueOf(Course.getTotalCourses())));
        
        panel.add(statsPanel, BorderLayout.CENTER);
        
        // Quick actions panel
        JPanel actionsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        actionsPanel.setBorder(BorderFactory.createTitledBorder("Quick Actions"));
        
        JButton addStudentBtn = new JButton("Add Student");
        addStudentBtn.addActionListener(e -> tabbedPane.setSelectedIndex(1)); // Go to Students tab
        
        JButton addCourseBtn = new JButton("Add Course");
        addCourseBtn.addActionListener(e -> tabbedPane.setSelectedIndex(2)); // Go to Courses tab
        
        actionsPanel.add(addStudentBtn);
        actionsPanel.add(addCourseBtn);
        
        panel.add(actionsPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    public static void main(String[] args) {
        try {
            // Set system look and feel
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Launch the application
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
} 