import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class StudentDashboardConnected extends JFrame {

    private JTable lessonTable;
    private DefaultTableModel tableModel;
    private JButton viewButton, refreshButton, searchButton;
    private JTextField searchField;

    public StudentDashboardConnected() {
        setTitle("Student Dashboard - Lab07");
        setSize(800, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Top Panel - Search
        JPanel topPanel = new JPanel();
        searchField = new JTextField(20);
        searchButton = new JButton("Search");
        topPanel.add(new JLabel("Search:"));
        topPanel.add(searchField);
        topPanel.add(searchButton);

        // Table
        tableModel = new DefaultTableModel(new String[]{"Lesson ID", "Title"}, 0);
        lessonTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(lessonTable);

        // Bottom Panel - Buttons
        JPanel buttonPanel = new JPanel();
        viewButton = new JButton("View Lesson");
        refreshButton = new JButton("Refresh List");
        buttonPanel.add(viewButton);
        buttonPanel.add(refreshButton);

        // Add panels to frame
        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Load lessons
        loadLessons();

        // Button actions
        viewButton.addActionListener(e -> viewLesson());
        refreshButton.addActionListener(e -> loadLessons());
        searchButton.addActionListener(e -> searchLessons());
    }

     /* Load all lessons from backend*/
    private void loadLessons() {
        tableModel.setRowCount(0); // Clear table
        List<Lesson> lessons = LessonBackend.loadLessons();
        for (Lesson lesson : lessons) {
            tableModel.addRow(new Object[]{lesson.getLessonId(), lesson.getTitle()});
        }
    }

    /*Search lessons by keyword in title or content
     */
    private void searchLessons() {
        String keyword = searchField.getText().trim().toLowerCase();
        tableModel.setRowCount(0);
        List<Lesson> lessons = LessonBackend.loadLessons();
        for (Lesson lesson : lessons) {
            if (lesson.getTitle().toLowerCase().contains(keyword) || lesson.getContent().toLowerCase().contains(keyword)) {
                tableModel.addRow(new Object[]{lesson.getLessonId(), lesson.getTitle()});
            }
        }
    }

    /*View lesson details
     */
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
        SwingUtilities.invokeLater(() -> new StudentDashboardConnected().setVisible(true));
    }
}
