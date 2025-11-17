package Views;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import Models.*;
import Controllers.*;

public class StudentDashboard extends JFrame {
    private final Student student;
    private final CourseController courseController;

    private JTable lessonTable;
    private DefaultTableModel tableModel;
    private JButton viewButton, refreshButton, searchButton;
    private JTextField searchField;

    public StudentDashboard(Student student, CourseController courseController) {
        this.student = student;
        this.courseController = courseController;

        setTitle("Student Dashboard - " + student.getUsername());
        setSize(800, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel topPanel = new JPanel();
        searchField = new JTextField(20);
        searchButton = new JButton("Search");
        topPanel.add(new JLabel("Search:"));
        topPanel.add(searchField);
        topPanel.add(searchButton);

        tableModel = new DefaultTableModel(new String[]{"Course", "Lesson ID", "Title"}, 0);
        lessonTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(lessonTable);

        JPanel buttonPanel = new JPanel();
        viewButton = new JButton("View Lesson");
        refreshButton = new JButton("Refresh List");
        buttonPanel.add(viewButton);
        buttonPanel.add(refreshButton);

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // loadLessons();

        // viewButton.addActionListener(e -> viewLesson());
        // refreshButton.addActionListener(e -> loadLessons());
        // searchButton.addActionListener(e -> searchLessons());
    }

    // private void loadLessons() {
    //     tableModel.setRowCount(0); // Clear table

    //     for (String courseId : student.getEnrolledCourses()) {
    //         Course course = courseController.getAllCourses().stream()
    //                 .filter(c -> c.getCourseId().equals(courseId))
    //                 .findFirst().orElse(null);
    //         if (course == null) continue;

    //         for (String lessonId : course.getLessons()) {
    //             Lesson lesson = courseController.getLessonById(lessonId);
    //             if (lesson != null) {
    //                 tableModel.addRow(new Object[]{course.getTitle(), lesson.getLessonId(), lesson.getTitle()});
    //             }
    //         }
    //     }
    // }

    // private void searchLessons() {
    //     String keyword = searchField.getText().trim().toLowerCase();
    //     tableModel.setRowCount(0);

    //     for (String courseId : student.getEnrolledCourses()) {
    //         Course course = courseController.getAllCourses().stream()
    //                 .filter(c -> c.getCourseId().equals(courseId))
    //                 .findFirst().orElse(null);
    //         if (course == null) continue;

    //         for (String lessonId : course.getLessons()) {
    //             Lesson lesson = courseController.getLessonById(lessonId);
    //             if (lesson != null &&
    //                     (lesson.getTitle().toLowerCase().contains(keyword) ||
    //                      lesson.getContent().toLowerCase().contains(keyword))) {
    //                 tableModel.addRow(new Object[]{course.getTitle(), lesson.getLessonId(), lesson.getTitle()});
    //             }
    //         }
    //     }
    // }

    // private void viewLesson() {
    //     int selectedRow = lessonTable.getSelectedRow();
    //     if (selectedRow == -1) {
    //         JOptionPane.showMessageDialog(this, "Select a lesson to view.", "Error", JOptionPane.ERROR_MESSAGE);
    //         return;
    //     }

    //     String lessonId = (String) tableModel.getValueAt(selectedRow, 1); // Column 1 = lessonId
    //     Lesson lesson = courseController.getLessonById(lessonId);
    //     if (lesson != null) {
    //         String resources = lesson.getResources() == null ? "" : String.join("\n", lesson.getResources());
    //         JOptionPane.showMessageDialog(this,
    //                 "Title: " + lesson.getTitle() +
    //                 "\n\nContent:\n" + lesson.getContent() +
    //                 "\n\nResources:\n" + resources,
    //                 "Lesson Details",
    //                 JOptionPane.INFORMATION_MESSAGE);
    //     }
    // }
}
