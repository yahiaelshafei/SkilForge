import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class InstructorDashboard extends JFrame {

    private JTable lessonTable;
    private DefaultTableModel tableModel;
    private JButton addButton, editButton, deleteButton, viewButton, refreshButton;

    public InstructorDashboard() {
        setTitle("Instructor Dashboard - Lab07");
        setSize(900, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Table
        tableModel = new DefaultTableModel(new String[]{"Lesson ID", "Title"}, 0);
        lessonTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(lessonTable);

        // Buttons
        addButton = new JButton("Add Lesson");
        editButton = new JButton("Edit Lesson");
        deleteButton = new JButton("Delete Lesson");
        viewButton = new JButton("View Lesson");
        refreshButton = new JButton("Refresh List");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(viewButton);
        buttonPanel.add(refreshButton);

        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Load lessons
        loadLessons();

        // Button actions
        addButton.addActionListener(e -> addLesson());
        editButton.addActionListener(e -> editLesson());
        deleteButton.addActionListener(e -> deleteLesson());
        viewButton.addActionListener(e -> viewLesson());
        refreshButton.addActionListener(e -> loadLessons());
    }

    private void loadLessons() {
        tableModel.setRowCount(0);
        List<Lesson> lessons = LessonBackend.loadLessons();
        for (Lesson lesson : lessons) {
            tableModel.addRow(new Object[]{lesson.getLessonId(), lesson.getTitle()});
        }
    }

    private void addLesson() {
        LessonDialog dialog = new LessonDialog(this, null);
        dialog.setVisible(true);
        if (dialog.isSaved()) {
            LessonBackend.addLesson(dialog.getLesson());
            loadLessons();
        }
    }

    private void editLesson() {
        int selectedRow = lessonTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Select a lesson to edit.");
            return;
        }
        String lessonId = (String) tableModel.getValueAt(selectedRow, 0);
        Lesson lesson = LessonBackend.getLessonById(lessonId);
        if (lesson != null) {
            LessonDialog dialog = new LessonDialog(this, lesson);
            dialog.setVisible(true);
            if (dialog.isSaved()) {
                LessonBackend.updateLesson(dialog.getLesson());
                loadLessons();
            }
        }
    }

    private void deleteLesson() {
        int selectedRow = lessonTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Select a lesson to delete.");
            return;
        }
        String lessonId = (String) tableModel.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete this lesson?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            LessonBackend.deleteLesson(lessonId);
            loadLessons();
        }
    }

    private void viewLesson() {
        int selectedRow = lessonTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Select a lesson to view.");
            return;
        }
        String lessonId = (String) tableModel.getValueAt(selectedRow, 0);
        Lesson lesson = LessonBackend.getLessonById(lessonId);
        if (lesson != null) {
            String resources = String.join("\n", lesson.getResources());
            JOptionPane.showMessageDialog(this,
                    "Title: " + lesson.getTitle() + "\n\nContent:\n" + lesson.getContent() +
                            "\n\nResources:\n" + resources,
                    "Lesson Details",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new InstructorDashboard().setVisible(true));
    }
}
