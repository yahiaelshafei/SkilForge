package Views;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.*;
import Models.*;
import Controllers.*;

public class StudentDashboard extends JFrame {
    private final Student student;
    private final CourseController courseController;

    private JTabbedPane tabbedPane;
    
    // Available Courses Tab
    private JTable availableCoursesTable;
    private DefaultTableModel availableCoursesModel;
    private JButton enrollButton, refreshAvailableButton;
    
    // My Courses Tab
    private JTable myCoursesTable;
    private DefaultTableModel myCoursesModel;
    private JButton viewLessonsButton, refreshMyCoursesButton;
    
    // Lessons Tab
    private JTable lessonsTable;
    private DefaultTableModel lessonsModel;
    private JButton viewLessonButton, markCompleteButton, backToCoursesButton;
    private JComboBox<String> courseSelector;
    private JLabel progressLabel;
    
    private JButton logoutButton;

    public StudentDashboard(Student student, CourseController courseController) {
        this.student = student;
        this.courseController = courseController;

        setTitle("Student Dashboard - " + student.getUsername());
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        tabbedPane = new JTabbedPane();
        
        // Create tabs
        tabbedPane.addTab("Available Courses", createAvailableCoursesPanel());
        tabbedPane.addTab("My Courses", createMyCoursesPanel());
        tabbedPane.addTab("Lessons", createLessonsPanel());
        
        add(tabbedPane, BorderLayout.CENTER);
        
        // Bottom panel with logout
        JPanel bottomPanel = new JPanel();
        logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> logout());
        bottomPanel.add(logoutButton);
        add(bottomPanel, BorderLayout.SOUTH);

        loadAvailableCourses();
        loadMyCourses();
    }

    private JPanel createAvailableCoursesPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Top panel with refresh button
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        refreshAvailableButton = new JButton("Refresh");
        refreshAvailableButton.addActionListener(e -> loadAvailableCourses());
        topPanel.add(new JLabel("Browse Available Courses"));
        topPanel.add(refreshAvailableButton);
        panel.add(topPanel, BorderLayout.NORTH);
        
        // Table
        availableCoursesModel = new DefaultTableModel(
            new String[]{"Course ID", "Title", "Description", "Instructor"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        availableCoursesTable = new JTable(availableCoursesModel);
        availableCoursesTable.getColumnModel().getColumn(2).setPreferredWidth(300);
        JScrollPane scrollPane = new JScrollPane(availableCoursesTable);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Bottom panel with enroll button
        JPanel bottomPanel = new JPanel();
        enrollButton = new JButton("Enroll in Selected Course");
        enrollButton.addActionListener(e -> enrollInCourse());
        bottomPanel.add(enrollButton);
        panel.add(bottomPanel, BorderLayout.SOUTH);
        
        return panel;
    }

    private JPanel createMyCoursesPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Top panel
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        refreshMyCoursesButton = new JButton("Refresh");
        refreshMyCoursesButton.addActionListener(e -> loadMyCourses());
        topPanel.add(new JLabel("My Enrolled Courses"));
        topPanel.add(refreshMyCoursesButton);
        panel.add(topPanel, BorderLayout.NORTH);
        
        // Table
        myCoursesModel = new DefaultTableModel(
            new String[]{"Course ID", "Title", "Description", "Progress"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        myCoursesTable = new JTable(myCoursesModel);
        myCoursesTable.getColumnModel().getColumn(2).setPreferredWidth(300);
        JScrollPane scrollPane = new JScrollPane(myCoursesTable);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Bottom panel
        JPanel bottomPanel = new JPanel();
        viewLessonsButton = new JButton("View Lessons");
        viewLessonsButton.addActionListener(e -> viewCourseLessons());
        bottomPanel.add(viewLessonsButton);
        panel.add(bottomPanel, BorderLayout.SOUTH);
        
        return panel;
    }

    private JPanel createLessonsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Top panel with course selector
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(new JLabel("Select Course:"));
        courseSelector = new JComboBox<>();
        courseSelector.setPreferredSize(new Dimension(300, 25));
        courseSelector.addActionListener(e -> loadLessonsForCourse());
        topPanel.add(courseSelector);
        
        progressLabel = new JLabel("Progress: 0/0 lessons completed");
        topPanel.add(progressLabel);
        
        backToCoursesButton = new JButton("Back to My Courses");
        backToCoursesButton.addActionListener(e -> tabbedPane.setSelectedIndex(1));
        topPanel.add(backToCoursesButton);
        
        panel.add(topPanel, BorderLayout.NORTH);
        
        // Table
        lessonsModel = new DefaultTableModel(
            new String[]{"Lesson ID", "Title", "Status"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        lessonsTable = new JTable(lessonsModel);
        JScrollPane scrollPane = new JScrollPane(lessonsTable);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Bottom panel
        JPanel bottomPanel = new JPanel();
        viewLessonButton = new JButton("View Lesson Details");
        viewLessonButton.addActionListener(e -> viewLessonDetails());
        markCompleteButton = new JButton("Mark as Complete");
        markCompleteButton.addActionListener(e -> markLessonComplete());
        bottomPanel.add(viewLessonButton);
        bottomPanel.add(markCompleteButton);
        panel.add(bottomPanel, BorderLayout.SOUTH);
        
        return panel;
    }

    private void loadAvailableCourses() {
        availableCoursesModel.setRowCount(0);
        java.util.List<Course> allCourses = courseController.getAllCourses();
        
        for (Course course : allCourses) {
            // Only show courses the student is not enrolled in
            if (!student.getEnrolledCourses().contains(course.getCourseId())) {
                String instructorName = "Unknown";
                User instructor = courseController.getCourseById(course.getCourseId()) != null 
                    ? null : null; // You might want to add a method to get instructor name
                
                availableCoursesModel.addRow(new Object[]{
                    course.getCourseId(),
                    course.getTitle(),
                    course.getDescription(),
                    course.getInstructorId()
                });
            }
        }
    }

    private void loadMyCourses() {
        myCoursesModel.setRowCount(0);
        
        for (String courseId : student.getEnrolledCourses()) {
            Course course = courseController.getCourseById(courseId);
            if (course != null) {
                int totalLessons = course.getLessons().size();
                int completedLessons = courseController.getCompletedLessonsCount(student.getUserId(), courseId);
                String progress = completedLessons + "/" + totalLessons;
                
                myCoursesModel.addRow(new Object[]{
                    course.getCourseId(),
                    course.getTitle(),
                    course.getDescription(),
                    progress
                });
            }
        }
        
        // Update course selector in lessons tab
        courseSelector.removeAllItems();
        for (String courseId : student.getEnrolledCourses()) {
            Course course = courseController.getCourseById(courseId);
            if (course != null) {
                courseSelector.addItem(course.getTitle() + " | " + course.getCourseId());
            }
        }
    }

    private void enrollInCourse() {
        int selectedRow = availableCoursesTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, 
                "Please select a course to enroll!", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String courseId = (String) availableCoursesModel.getValueAt(selectedRow, 0);
        String courseTitle = (String) availableCoursesModel.getValueAt(selectedRow, 1);
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Do you want to enroll in: " + courseTitle + "?", 
            "Confirm Enrollment", 
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            if (courseController.enrollStudent(courseId, student.getUserId())) {
                JOptionPane.showMessageDialog(this, 
                    "Successfully enrolled in " + courseTitle + "!", 
                    "Success", 
                    JOptionPane.INFORMATION_MESSAGE);
                loadAvailableCourses();
                loadMyCourses();
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Failed to enroll in course.", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void viewCourseLessons() {
        int selectedRow = myCoursesTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, 
                "Please select a course!", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String courseId = (String) myCoursesModel.getValueAt(selectedRow, 0);
        String courseTitle = (String) myCoursesModel.getValueAt(selectedRow, 1);
        
        // Set the course selector to this course
        for (int i = 0; i < courseSelector.getItemCount(); i++) {
            String item = courseSelector.getItemAt(i);
            if (item.contains(courseId)) {
                courseSelector.setSelectedIndex(i);
                break;
            }
        }
        
        // Switch to lessons tab
        tabbedPane.setSelectedIndex(2);
        loadLessonsForCourse();
    }

    private void loadLessonsForCourse() {
        lessonsModel.setRowCount(0);
        
        String selected = (String) courseSelector.getSelectedItem();
        if (selected == null) {
            progressLabel.setText("Progress: 0/0 lessons completed");
            return;
        }
        
        String courseId = selected.split("\\|")[1].trim();
        java.util.List<Lesson> lessons = courseController.getCourseLessons(courseId);
        
        int completedCount = 0;
        for (Lesson lesson : lessons) {
            boolean isCompleted = courseController.isLessonCompleted(
                student.getUserId(), courseId, lesson.getLessonId());
            
            String status = isCompleted ? "✓ Completed" : "Not Completed";
            if (isCompleted) completedCount++;
            
            lessonsModel.addRow(new Object[]{
                lesson.getLessonId(),
                lesson.getTitle(),
                status
            });
        }
        
        progressLabel.setText("Progress: " + completedCount + "/" + lessons.size() + " lessons completed");
    }

    private void viewLessonDetails() {
        int selectedRow = lessonsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, 
                "Please select a lesson to view!", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String lessonId = (String) lessonsModel.getValueAt(selectedRow, 0);
        Lesson lesson = courseController.getLessonById(lessonId);
        
        if (lesson == null) {
            JOptionPane.showMessageDialog(this, 
                "Lesson not found!", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Create a dialog to show lesson details
        JDialog dialog = new JDialog(this, "Lesson: " + lesson.getTitle(), true);
        dialog.setSize(600, 400);
        dialog.setLocationRelativeTo(this);
        
        JPanel contentPanel = new JPanel(new BorderLayout(10, 10));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Title
        JLabel titleLabel = new JLabel(lesson.getTitle());
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        contentPanel.add(titleLabel, BorderLayout.NORTH);
        
        // Content
        JTextArea contentArea = new JTextArea(lesson.getContent());
        contentArea.setEditable(false);
        contentArea.setLineWrap(true);
        contentArea.setWrapStyleWord(true);
        contentArea.setFont(new Font("Arial", Font.PLAIN, 14));
        JScrollPane contentScroll = new JScrollPane(contentArea);
        contentPanel.add(contentScroll, BorderLayout.CENTER);
        
        // Resources
        if (lesson.getResources() != null && !lesson.getResources().isEmpty()) {
            JPanel resourcesPanel = new JPanel(new BorderLayout());
            resourcesPanel.add(new JLabel("Resources:"), BorderLayout.NORTH);
            
            JTextArea resourcesArea = new JTextArea(String.join("\n", lesson.getResources()));
            resourcesArea.setEditable(false);
            resourcesArea.setFont(new Font("Arial", Font.PLAIN, 12));
            JScrollPane resourcesScroll = new JScrollPane(resourcesArea);
            resourcesScroll.setPreferredSize(new Dimension(0, 80));
            resourcesPanel.add(resourcesScroll, BorderLayout.CENTER);
            
            contentPanel.add(resourcesPanel, BorderLayout.SOUTH);
        }
        
        dialog.add(contentPanel);
        dialog.setVisible(true);
    }

    private void markLessonComplete() {
        int selectedRow = lessonsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, 
                "Please select a lesson to mark as complete!", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String lessonId = (String) lessonsModel.getValueAt(selectedRow, 0);
        String selected = (String) courseSelector.getSelectedItem();
        String courseId = selected.split("\\|")[1].trim();
        
        if (courseController.isLessonCompleted(student.getUserId(), courseId, lessonId)) {
            JOptionPane.showMessageDialog(this, 
                "This lesson is already marked as complete!", 
                "Info", 
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        if (courseController.markLessonComplete(student.getUserId(), courseId, lessonId)) {
            JOptionPane.showMessageDialog(this, 
                "Lesson marked as complete!", 
                "Success", 
                JOptionPane.INFORMATION_MESSAGE);
            loadLessonsForCourse();
            loadMyCourses(); // Refresh progress in My Courses tab
        } else {
            JOptionPane.showMessageDialog(this, 
                "Failed to mark lesson as complete.", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to logout?", 
            "Confirm Logout", 
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            dispose();
            new LoginScreen(new UserDatabase("src/Data/users.json"), courseController).setVisible(true);
        }
    }
}