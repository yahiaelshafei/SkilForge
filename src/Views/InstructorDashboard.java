package Views;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.*;
import Controllers.*;
import Models.*;

public class InstructorDashboard extends JFrame {
    private Instructor instructor;
    private final String instructorId;
    private final CourseController courseController;

    private JTable lessonTable;
    private DefaultTableModel lessonTableModel;
    private JButton addLessonButton, editLessonButton, deleteLessonButton, viewStudentsButton;
    private JButton addCourseButton, editCourseButton, deleteCourseButton, refreshButton, logoutButton;
    private JComboBox<String> courseBox;

    public InstructorDashboard(Instructor instructor, CourseController courseController) {
        this.instructor = instructor;
        this.instructorId = instructor.getUserId();
        this.courseController = courseController;

        setTitle("Instructor Dashboard - " + instructor.getUsername());
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Top Panel - Course Selection
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(new JLabel("Select Course:"));
        courseBox = new JComboBox<>();
        courseBox.setPreferredSize(new Dimension(300, 25));
        topPanel.add(courseBox);
        
        refreshButton = new JButton("Refresh");
        addCourseButton = new JButton("Add Course");
        editCourseButton = new JButton("Edit Course");
        deleteCourseButton = new JButton("Delete Course");
        logoutButton = new JButton("Logout");
        
        topPanel.add(refreshButton);
        topPanel.add(addCourseButton);
        topPanel.add(editCourseButton);
        topPanel.add(deleteCourseButton);
        topPanel.add(logoutButton);
        add(topPanel, BorderLayout.NORTH);

        // Center Panel - Lessons Table
        lessonTableModel = new DefaultTableModel(new String[] { "Lesson ID", "Title", "Content Preview" }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        lessonTable = new JTable(lessonTableModel);
        JScrollPane scrollPane = new JScrollPane(lessonTable);
        add(scrollPane, BorderLayout.CENTER);

        // Bottom Panel - Lesson Management Buttons
        JPanel buttonPanel = new JPanel();
        addLessonButton = new JButton("Add Lesson");
        editLessonButton = new JButton("Edit Lesson");
        deleteLessonButton = new JButton("Delete Lesson");
        viewStudentsButton = new JButton("View Enrolled Students");
        
        buttonPanel.add(addLessonButton);
        buttonPanel.add(editLessonButton);
        buttonPanel.add(deleteLessonButton);
        buttonPanel.add(viewStudentsButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Event Listeners
        courseBox.addActionListener(e -> loadLessons());
        refreshButton.addActionListener(e -> {
            refreshInstructorData();
            loadCourses();
            loadLessons();
        });
        addCourseButton.addActionListener(e -> addCourse());
        editCourseButton.addActionListener(e -> editCourse());
        deleteCourseButton.addActionListener(e -> deleteCourse());
        addLessonButton.addActionListener(e -> addLesson());
        editLessonButton.addActionListener(e -> editLesson());
        deleteLessonButton.addActionListener(e -> deleteLesson());
        viewStudentsButton.addActionListener(e -> viewEnrolledStudents());
        logoutButton.addActionListener(e -> logout());

        loadCourses();
    }

    private void loadCourses() {
        courseBox.removeAllItems();
        
        // Use a Set to track course IDs we've already added (prevent duplicates)
        java.util.Set<String> addedCourses = new java.util.HashSet<>();
        
        // Create a copy of the list to safely remove duplicates
        java.util.List<String> createdCourses = new java.util.ArrayList<>(instructor.getCreatedCourses());
        boolean foundDuplicates = false;
        
        for (String courseId : createdCourses) {
            // Skip if we've already added this course
            if (addedCourses.contains(courseId)) {
                foundDuplicates = true;
                continue;
            }
            
            Course c = courseController.getCourseById(courseId);
            if (c != null) {
                courseBox.addItem(c.getTitle() + " | " + c.getCourseId());
                addedCourses.add(courseId);
            }
        }
        
        // If we found duplicates, clean up the instructor's list
        if (foundDuplicates) {
            instructor.setCreatedCourses(new java.util.ArrayList<>(addedCourses));
            // Note: We'll save this when the user adds/edits a course next time
            System.out.println("Removed duplicate courses from instructor's list");
        }
        
        if (courseBox.getItemCount() > 0) {
            loadLessons();
        }
    }

    private void loadLessons() {
        lessonTableModel.setRowCount(0);
        String selected = (String) courseBox.getSelectedItem();
        if (selected == null) return;

        String courseId = selected.split("\\|")[1].trim();
        java.util.List<Lesson> lessons = courseController.getCourseLessons(courseId);
        
        for (Lesson lesson : lessons) {
            String preview = lesson.getContent().length() > 50 
                ? lesson.getContent().substring(0, 50) + "..." 
                : lesson.getContent();
            lessonTableModel.addRow(new Object[]{
                lesson.getLessonId(), 
                lesson.getTitle(), 
                preview
            });
        }
    }

    private void addCourse() {
        JTextField titleField = new JTextField(20);
        JTextArea descriptionField = new JTextArea(4, 20);
        descriptionField.setLineWrap(true);
        descriptionField.setWrapStyleWord(true);
        
        Object[] fields = {
                "Course Title:", titleField,
                "Course Description:", new JScrollPane(descriptionField)
        };

        int option = JOptionPane.showConfirmDialog(this, fields, "Add New Course", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String title = titleField.getText().trim();
            String description = descriptionField.getText().trim();

            if (title.isEmpty() || description.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all fields!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String courseId = "course-" + System.currentTimeMillis();
            Course course = new Course(courseId, title, description, instructor.getUserId(),
                    new ArrayList<>(), new ArrayList<>());

            if (courseController.addCourse(course)) {
                // Don't manually add to createdCourses - the controller does this now
                // Just refresh the UI
                JOptionPane.showMessageDialog(this, "Course added successfully!");
                
                // Reload instructor from database to get updated createdCourses
                refreshInstructorData();
                loadCourses();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add course.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void editCourse() {
        String selected = (String) courseBox.getSelectedItem();
        if (selected == null) {
            JOptionPane.showMessageDialog(this, "Please select a course to edit!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String courseId = selected.split("\\|")[1].trim();
        Course course = courseController.getCourseById(courseId);
        if (course == null) return;

        JTextField titleField = new JTextField(course.getTitle(), 20);
        JTextArea descriptionField = new JTextArea(course.getDescription(), 4, 20);
        descriptionField.setLineWrap(true);
        descriptionField.setWrapStyleWord(true);
        
        Object[] fields = {
                "Course Title:", titleField,
                "Course Description:", new JScrollPane(descriptionField)
        };

        int option = JOptionPane.showConfirmDialog(this, fields, "Edit Course", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String title = titleField.getText().trim();
            String description = descriptionField.getText().trim();

            if (title.isEmpty() || description.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all fields!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            course.setTitle(title);
            course.setDescription(description);
            courseController.updateCourse(course);
            JOptionPane.showMessageDialog(this, "Course updated successfully!");
            loadCourses();
        }
    }

    private void deleteCourse() {
        String selected = (String) courseBox.getSelectedItem();
        if (selected == null) {
            JOptionPane.showMessageDialog(this, "Please select a course to delete!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to delete this course? This will also delete all lessons.", 
            "Confirm Delete", 
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            String courseId = selected.split("\\|")[1].trim();
            if (courseController.deleteCourse(courseId)) {
                instructor.getCreatedCourses().remove(courseId);
                JOptionPane.showMessageDialog(this, "Course deleted successfully!");
                loadCourses();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete course.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void addLesson() {
        String selected = (String) courseBox.getSelectedItem();
        if (selected == null) {
            JOptionPane.showMessageDialog(this, "Please select a course first!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JTextField titleField = new JTextField(20);
        JTextArea contentField = new JTextArea(5, 20);
        contentField.setLineWrap(true);
        contentField.setWrapStyleWord(true);
        JTextField resourcesField = new JTextField(20);
        
        Object[] fields = {
                "Lesson Title:", titleField,
                "Lesson Content:", new JScrollPane(contentField),
                "Resources (comma-separated URLs):", resourcesField
        };

        int option = JOptionPane.showConfirmDialog(this, fields, "Add New Lesson", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String title = titleField.getText().trim();
            String content = contentField.getText().trim();
            String resourcesStr = resourcesField.getText().trim();

            if (title.isEmpty() || content.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Title and Content are required!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            java.util.List<String> resources = new ArrayList<>();
            if (!resourcesStr.isEmpty()) {
                resources = Arrays.asList(resourcesStr.split(","));
                for (int i = 0; i < resources.size(); i++) {
                    resources.set(i, resources.get(i).trim());
                }
            }

            String lessonId = "lesson-" + System.currentTimeMillis();
            String courseId = selected.split("\\|")[1].trim();
            Lesson lesson = new Lesson(lessonId, title, content, resources);

            if (courseController.addLesson(courseId, lesson)) {
                JOptionPane.showMessageDialog(this, "Lesson added successfully!");
                loadLessons();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add lesson.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void editLesson() {
        int selectedRow = lessonTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a lesson to edit!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String lessonId = (String) lessonTableModel.getValueAt(selectedRow, 0);
        Lesson lesson = courseController.getLessonById(lessonId);
        if (lesson == null) return;

        JTextField titleField = new JTextField(lesson.getTitle(), 20);
        JTextArea contentField = new JTextArea(lesson.getContent(), 5, 20);
        contentField.setLineWrap(true);
        contentField.setWrapStyleWord(true);
        String resourcesStr = lesson.getResources() != null ? String.join(", ", lesson.getResources()) : "";
        JTextField resourcesField = new JTextField(resourcesStr, 20);
        
        Object[] fields = {
                "Lesson Title:", titleField,
                "Lesson Content:", new JScrollPane(contentField),
                "Resources (comma-separated URLs):", resourcesField
        };

        int option = JOptionPane.showConfirmDialog(this, fields, "Edit Lesson", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String title = titleField.getText().trim();
            String content = contentField.getText().trim();
            String resources = resourcesField.getText().trim();

            if (title.isEmpty() || content.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Title and Content are required!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            java.util.List<String> resourcesList = new ArrayList<>();
            if (!resources.isEmpty()) {
                resourcesList = Arrays.asList(resources.split(","));
                for (int i = 0; i < resourcesList.size(); i++) {
                    resourcesList.set(i, resourcesList.get(i).trim());
                }
            }

            lesson.setTitle(title);
            lesson.setContent(content);
            lesson.setResources(resourcesList);

            if (courseController.updateLesson(lesson)) {
                JOptionPane.showMessageDialog(this, "Lesson updated successfully!");
                loadLessons();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update lesson.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void deleteLesson() {
        int selectedRow = lessonTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a lesson to delete!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to delete this lesson?", 
            "Confirm Delete", 
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            String lessonId = (String) lessonTableModel.getValueAt(selectedRow, 0);
            String selected = (String) courseBox.getSelectedItem();
            String courseId = selected.split("\\|")[1].trim();

            if (courseController.deleteLesson(courseId, lessonId)) {
                JOptionPane.showMessageDialog(this, "Lesson deleted successfully!");
                loadLessons();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete lesson.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void viewEnrolledStudents() {
        String selected = (String) courseBox.getSelectedItem();
        if (selected == null) {
            JOptionPane.showMessageDialog(this, "Please select a course first!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String courseId = selected.split("\\|")[1].trim();
        java.util.List<Student> students = courseController.getEnrolledStudents(courseId);

        if (students.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No students enrolled in this course yet.", 
                "Enrolled Students", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String[] columns = {"Student ID", "Username", "Email", "Completed Lessons"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        
        for (Student s : students) {
            int completedCount = courseController.getCompletedLessonsCount(s.getUserId(), courseId);
            model.addRow(new Object[]{
                s.getUserId(),
                s.getUsername(),
                s.getEmail(),
                completedCount
            });
        }

        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(600, 300));

        JOptionPane.showMessageDialog(this, scrollPane, "Enrolled Students", JOptionPane.INFORMATION_MESSAGE);
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

    // Helper method to refresh instructor data from database
    private void refreshInstructorData() {
        User u = courseController.getUserById(instructorId);
        if (u instanceof Instructor) {
            this.instructor = (Instructor) u;
        }
    }
}