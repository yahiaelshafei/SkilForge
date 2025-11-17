// package Views;
// import javax.swing.*;

// import Models.Lesson;

// import java.awt.*;
// import java.util.Arrays;

// class LessonDialogue extends JDialog {

//     private JTextField idField, titleField;
//     private JTextArea contentArea, resourcesArea;
//     private JButton saveButton, cancelButton;
//     private boolean saved = false;
//     private Lesson lesson;

//     public LessonDialogue(JFrame parent, Lesson lesson) {
//         super(parent, "Lesson Details", true);
//         setSize(400, 400);
//         setLayout(new BorderLayout());
//         setLocationRelativeTo(parent);

//         this.lesson = lesson;

//         JPanel form = new JPanel(new GridLayout(4, 2, 5, 5));
//         idField = new JTextField();
//         titleField = new JTextField();
//         contentArea = new JTextArea(3, 20);
//         resourcesArea = new JTextArea(3, 20);

//         form.add(new JLabel("Lesson ID:"));
//         form.add(idField);
//         form.add(new JLabel("Title:"));
//         form.add(titleField);
//         form.add(new JLabel("Content:"));
//         form.add(new JScrollPane(contentArea));
//         form.add(new JLabel("Resources (comma separated):"));
//         form.add(new JScrollPane(resourcesArea));

//         add(form, BorderLayout.CENTER);

//         JPanel buttonPanel = new JPanel();
//         saveButton = new JButton("Save");
//         cancelButton = new JButton("Cancel");
//         buttonPanel.add(saveButton);
//         buttonPanel.add(cancelButton);
//         add(buttonPanel, BorderLayout.SOUTH);

//         if (lesson != null) {
//             idField.setText(lesson.getLessonId());
//             idField.setEditable(false);
//             titleField.setText(lesson.getTitle());
//             contentArea.setText(lesson.getContent());
//             resourcesArea.setText(String.join(", ", lesson.getResources()));
//         }

//         saveButton.addActionListener(e -> save());
//         cancelButton.addActionListener(e -> dispose());
//     }

//     private void save() {
//         String id = idField.getText().trim();
//         String title = titleField.getText().trim();
//         String content = contentArea.getText().trim();
//         String[] resources = resourcesArea.getText().split(",");

//         if (id.isEmpty() || title.isEmpty() || content.isEmpty()) {
//             JOptionPane.showMessageDialog(this, "ID, Title and Content cannot be empty.");
//             return;
//         }

//         if (lesson == null) lesson = new Lesson();

//         lesson.setLessonId(id);
//         lesson.setTitle(title);
//         lesson.setContent(content);
//         lesson.setResources(Arrays.asList(resources));

//         saved = true;
//         dispose();
//     }

//     public boolean isSaved() { return saved; }
//     public Lesson getLesson() { return lesson; }
// }
