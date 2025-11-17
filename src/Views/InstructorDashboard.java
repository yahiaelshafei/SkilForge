package Views;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import Controllers.*;
import Models.*;

public class InstructorDashboard extends JFrame {
    private final Instructor instructor;
    private final CourseController courseController;

    private JTable lessonTable;
    private DefaultTableModel tableModel;
    private JButton addButton, editButton, deleteButton, viewButton, refreshButton;
    private JComboBox<String> courseBox;

    public InstructorDashboard(Instructor instructor, CourseController courseController) {
        this.instructor = instructor;
        this.courseController = courseController;

        setTitle("Instructor Dashboard - " + instructor.getUsername());
        setSize(900, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel topPanel = new JPanel();
        courseBox = new JComboBox<>();
        refreshButton = new JButton("Refresh List");
        topPanel.add(new JLabel("Select Course:"));
        topPanel.add(courseBox);
        topPanel.add(refreshButton);
        add(topPanel, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(new String[] { "Lesson ID", "Title" }, 0);
        lessonTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(lessonTable);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        addButton = new JButton("Add Lesson");
        editButton = new JButton("Edit Lesson");
        deleteButton = new JButton("Delete Lesson");
        viewButton = new JButton("View Lesson");
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(viewButton);
        add(buttonPanel, BorderLayout.SOUTH);

        courseBox.addActionListener(e -> loadCourses());
        addButton.addActionListener(e -> addCourse());
        editButton.addActionListener(e -> editCourse());
        refreshButton.addActionListener(e -> {
            loadCourses();
        });

        if (courseBox.getItemCount() > 0)
            loadCourses();
    }

    private void loadCourses() {
        courseBox.removeAllItems();
        for (String courseId : instructor.getCreatedCourses()) {
            Course c = courseController.getAllCourses().stream()
                    .filter(course -> course.getCourseId().equals(courseId))
                    .findFirst().orElse(null);
            if (c != null) {
                courseBox.addItem(c.getTitle() + " | " + c.getCourseId());
            }
        }
    }

    private void addCourse() {
        JTextField titleField = new JTextField();
        JTextField descriptionField = new JTextField();
        Object[] fields = {
                "Course Title:", titleField,
                "Course Description:", descriptionField
        };

        int option = JOptionPane.showConfirmDialog(this, fields, "Add New Course", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String title = titleField.getText().trim();
            String description = descriptionField.getText().trim();

            if (title.isEmpty() || description.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Fill all fields!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String courseId = "course-" + System.currentTimeMillis();
            Course course = new Course(courseId, title, description, instructor.getUserId(),
                    new java.util.ArrayList<>(), new java.util.ArrayList<>());

            if (courseController.addCourse(course)) {
                instructor.getCreatedCourses().add(courseId);
                JOptionPane.showMessageDialog(this, "Course added successfully!");
                loadCourses();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add course.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void editCourse() {
        String selected = (String) courseBox.getSelectedItem();
        if (selected == null) {
            JOptionPane.showMessageDialog(this, "Select a course to edit!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String courseId = selected.split("\\|")[1].trim();
        Course course = courseController.getAllCourses().stream()
                .filter(c -> c.getCourseId().equals(courseId))
                .findFirst().orElse(null);

        if (course == null)
            return;

        JTextField titleField = new JTextField(course.getTitle());
        JTextField descriptionField = new JTextField(course.getDescription());
        Object[] fields = {
                "Course Title:", titleField,
                "Course Description:", descriptionField
        };

        int option = JOptionPane.showConfirmDialog(this, fields, "Edit Course", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String title = titleField.getText().trim();
            String description = descriptionField.getText().trim();

            if (title.isEmpty() || description.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Fill all fields!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            course.setTitle(title);
            course.setDescription(description);
            courseController.updateCourse(course);
            JOptionPane.showMessageDialog(this, "Course updated successfully!");
            loadCourses();
        }
    }

}
