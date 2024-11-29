/* import java.awt.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class EnhancedStudentCourseManagementSys extends JFrame {
    private CourseManager courseManager;intwr
    private Map<String, String> studentCourseMap;
    private static final Color BACKGROUND_COLOR = new Color(240, 248, 255);
    private static final Color PRIMARY_COLOR = new Color(41, 128, 185);
    private static final Color SECONDARY_COLOR = new Color(52, 152, 219);

    public EnhancedStudentCourseManagementSys() {
        // Initialize data structures
        courseManager = new CourseManager();
        studentCourseMap = new HashMap<>();

        // Pre-populate courses with more detailed information
        courseManager.addCourse(new Course("JAVA", "Advanced Java Programming", 
            "Comprehensive Java course covering core and advanced concepts", //syllabus cjcccna
            "Dr.Rajeshwar", 5));
        courseManager.addCourse(new Course("DBMS", "Database Management Systems", 
            "In-depth study of database design, management, and optimization", 
            "Surekha", 4));
        courseManager.addCourse(new Course("AI", "Artificial Intelligence", 
            "Machine learning, neural networks, and AI algorithms", 
            "Meghana", 6));
        courseManager.addCourse(new Course("SMF", "Statistical and Mathematical Foundations", 
            "Introduction to Probability", 
            "Dr.ShivaKumar", 5));  

        initializeFrame();
    }

    private void initializeFrame() {
        setTitle("💻 Student Course Management System");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // Fallback to default look and feel if system look and feel fails
            try {
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        tabbedPane.setBackground(BACKGROUND_COLOR);
        tabbedPane.setFont(new Font("Arial", Font.BOLD, 14));

        tabbedPane.addTab("📝 Student Registration", createRegistrationPanel());
        tabbedPane.addTab("👥 My Course", createMyCoursePanel());
        tabbedPane.addTab("📋 Enrolled Students", createEnrolledStudentsPanel());

        add(tabbedPane);
        setVisible(true);
    }

    private JPanel createRegistrationPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(BACKGROUND_COLOR);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("Student Registration", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(PRIMARY_COLOR);

        JComboBox<String> courseComboBox = new JComboBox<>();
        courseManager.getCourses().forEach(course -> 
            courseComboBox.addItem(course.getCourseId() + " - " + course.getCourseName()));

        JTextField studentIdField = new JTextField(15);
        JTextField studentNameField = new JTextField(15);

        JButton registerButton = createStyledButton("Register Student", PRIMARY_COLOR);
        registerButton.addActionListener(e -> {
            String selectedCourse = (String) courseComboBox.getSelectedItem();
            String studentId = studentIdField.getText().trim();
            String studentName = studentNameField.getText().trim();

            if (validateRegistration(selectedCourse, studentId, studentName)) {
                String courseId = selectedCourse.split(" - ")[0];
                Student student = new Student(studentId, studentName);
                courseManager.registerStudent(courseId, student);

                // Save the student-course mapping
                studentCourseMap.put(studentId, courseId);

                JOptionPane.showMessageDialog(this, 
                    "✅ Student Successfully Registered!", 
                    "Registration Confirmation", 
                    JOptionPane.INFORMATION_MESSAGE);

                clearRegistrationFields(studentIdField, studentNameField);
            }
        });

        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(titleLabel, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1;
        panel.add(new JLabel("Select Course:"), gbc);
        gbc.gridx = 1;
        panel.add(courseComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Student ID:"), gbc);
        gbc.gridx = 1;
        panel.add(studentIdField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(new JLabel("Student Name:"), gbc);
        gbc.gridx = 1;
        panel.add(studentNameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        panel.add(registerButton, gbc);

        return panel;
    }

    private JPanel createMyCoursePanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JTextField studentIdField = new JTextField(15);
        JButton viewCourseButton = createStyledButton("View My Course", SECONDARY_COLOR);

        JTextArea courseDetailsArea = new JTextArea();
        courseDetailsArea.setEditable(false);
        courseDetailsArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        courseDetailsArea.setBackground(Color.WHITE);
        courseDetailsArea.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        viewCourseButton.addActionListener(e -> {
            String studentId = studentIdField.getText().trim();
            if (studentCourseMap.containsKey(studentId)) {
                String courseId = studentCourseMap.get(studentId);
                Course course = courseManager.getCourseDetails(courseId);
                if (course != null) {
                    displayCourseDetails(course, courseDetailsArea);
                } else {
                    JOptionPane.showMessageDialog(this, 
                        "Course details not found!", 
                        "Error", 
                        JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, 
                    "No course registered for this student ID!", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        });

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBackground(BACKGROUND_COLOR);
        topPanel.add(new JLabel("Enter Student ID: "));
        topPanel.add(studentIdField);
        topPanel.add(viewCourseButton);

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(courseDetailsArea), BorderLayout.CENTER);

        return panel;
    }

    private JPanel createEnrolledStudentsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    
        JLabel titleLabel = new JLabel("Enrolled Students", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(PRIMARY_COLOR);
        panel.add(titleLabel, BorderLayout.NORTH);
    
        // Create a table to display enrolled students
        String[] columnNames = {"Course ID", "Course Name", "Student ID", "Student Name"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        JTable studentTable = new JTable(tableModel);
        studentTable.setFillsViewportHeight(true);
        studentTable.setFont(new Font("Arial", Font.PLAIN, 14));
        studentTable.setRowHeight(24);
    
        JScrollPane scrollPane = new JScrollPane(studentTable);
        scrollPane.setBackground(Color.WHITE);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        panel.add(scrollPane, BorderLayout.CENTER);
    
        // Load data into the table
        JButton loadButton = createStyledButton("Load Enrolled Students", SECONDARY_COLOR);
        loadButton.addActionListener(e -> {
            tableModel.setRowCount(0); // Clear existing data
            for (Course course : courseManager.getCourses()) {
                java.util.List<Student> students = course.getEnrolledStudents();
                for (Student student : students) {
                    tableModel.addRow(new Object[]{
                        course.getCourseId(),
                        course.getCourseName(),
                        student.getId(),
                        student.getName()
                    });
                }
            }
            if (tableModel.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this,
                    "No students are currently enrolled in any course!",
                    "Information",
                    JOptionPane.INFORMATION_MESSAGE);
            }
        });
    
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(BACKGROUND_COLOR);
        buttonPanel.add(loadButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);
    
        return panel;
    }

    private JButton createStyledButton(String text, Color backgroundColor) {
        JButton button = new JButton(text);
        button.setBackground(backgroundColor);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        return button;
    }

    private boolean validateRegistration(String course, String studentId, String studentName) {
        if (course == null || course.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Please select a course!", 
                "Registration Error", 
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (studentId.isEmpty() || studentName.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Student ID and Name cannot be empty!", 
                "Registration Error", 
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private void clearRegistrationFields(JTextField... fields) {
        for (JTextField field : fields) {
            field.setText("");
        }
    }

    private void displayCourseDetails(Course course, JTextArea displayArea) {
        if (course != null) {
            displayArea.setText(String.format(
                "📚 Course Details\n" +
                "---------------------\n" +
                "Course ID: %s\n" +
                "Course Name: %s\n" +
                "Faculty: %s\n" +
                "Credits: %d\n\n" +
                "📋 Syllabus Overview:\n" +
                "%s", 
                course.getCourseId(), 
                course.getCourseName(), 
                course.getFaculty(), 
                course.getCredits(), 
                course.getSyllabus()
            ));
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new EnhancedStudentCourseManagementSys());
    }
}

*/
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class StudentCourseManagementSystem extends JFrame {
    private JComboBox<String> courseComboBox;
    private JTextField studentIdField;
    private JTextField studentNameField;
    private JTextArea courseDetailsArea;
    private DefaultTableModel enrolledStudentsModel;

    public StudentCourseManagementSystem() {
        initializeComponents();
        setupLayout();
        loadCourses();
    }

    private void initializeComponents() {
        setTitle("Student Course Management System");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Course Selection Components
        courseComboBox = new JComboBox<>();
        studentIdField = new JTextField(15);
        studentNameField = new JTextField(15);

        // Course Details Area
        courseDetailsArea = new JTextArea();
        courseDetailsArea.setEditable(false);
        courseDetailsArea.setFont(new Font("Monospaced", Font.PLAIN, 14));

        // Enrolled Students Table
        String[] columnNames = {"Student ID", "Student Name"};
        enrolledStudentsModel = new DefaultTableModel(columnNames, 0);
        JTable enrolledStudentsTable = new JTable(enrolledStudentsModel);
    }

    private void setupLayout() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));

        // Registration Panel
        JPanel registrationPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0;
        gbc.gridy = 0;
        registrationPanel.add(new JLabel("Select Course:"), gbc);

        gbc.gridx = 1;
        registrationPanel.add(courseComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        registrationPanel.add(new JLabel("Student ID:"), gbc);

        gbc.gridx = 1;
        registrationPanel.add(studentIdField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        registrationPanel.add(new JLabel("Student Name:"), gbc);

        gbc.gridx = 1;
        registrationPanel.add(studentNameField, gbc);

        // Register Button
        JButton registerButton = new JButton("Register");
        registerButton.addActionListener(e -> registerStudent());

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        registrationPanel.add(registerButton, gbc);

        // Course Details Panel
        JPanel courseDetailsPanel = new JPanel(new BorderLayout());
        courseDetailsPanel.add(new JScrollPane(courseDetailsArea), BorderLayout.CENTER);

        // Enrolled Students Panel
        JPanel enrolledStudentsPanel = new JPanel(new BorderLayout());
        enrolledStudentsPanel.add(new JScrollPane(new JTable(enrolledStudentsModel)), BorderLayout.CENTER);

        // Tabbed Pane
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Registration", registrationPanel);
        tabbedPane.addTab("Course Details", courseDetailsPanel);
        tabbedPane.addTab("Enrolled Students", enrolledStudentsPanel);

        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        add(mainPanel);
    }

    private void loadCourses() {
        courseComboBox.removeAllItems();
        List<String[]> courses = DatabaseManager.getAllCourses();
        for (String[] course : courses) {
            courseComboBox.addItem(course[0] + " - " + course[1]);
        }
    }

    private void registerStudent() {
        // Get inputs from GUI
        String selectedCourse = (String) courseComboBox.getSelectedItem();
        String studentId = studentIdField.getText().trim();
        String studentName = studentNameField.getText().trim();

        // Validate inputs
        if (selectedCourse == null || studentId.isEmpty() || studentName.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please fill all fields!",
                "Registration Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Extract Course ID from the selection
        String courseId = selectedCourse.split(" - ")[0];

        // Use the console-based registration logic
        try {
            registerCourse(studentId, studentName, courseId);

            // Show success message
            JOptionPane.showMessageDialog(this,
                "Student Registered Successfully!",
                "Registration Successful",
                JOptionPane.INFORMATION_MESSAGE);

            // Clear fields
            studentIdField.setText("");
            studentNameField.setText("");

            // Refresh course details and enrolled students
            updateCourseDetails(courseId);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Registration Failed: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateCourseDetails(String courseId) {
        // Update course details area
        String[] details = DatabaseManager.getCourseDetails(courseId);
        if (details != null) {
            courseDetailsArea.setText(String.format(
                "Course Name: %s\n" +
                "Faculty: %s\n" +
                "Credits: %s\n\n" +
                "Syllabus:\n%s",
                details[0], details[1], details[2], details[3]
            ));
        }

        // Update enrolled students table
        enrolledStudentsModel.setRowCount(0);
        List<String[]> students = DatabaseManager.getEnrolledStudents(courseId);
        for (String[] student : students) {
            enrolledStudentsModel.addRow(student);
        }
    }

    // Existing console-based method for registration
    public static void registerCourse(String studentId, String studentName, String courseId) {
        String insertQuery = "INSERT INTO student_enrollments (courseId, studentId, studentName) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(insertQuery)) {

            // Set values for the prepared statement
            stmt.setString(1, courseId);
            stmt.setString(2, studentId);
            stmt.setString(3, studentName);

            // Execute the query
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Course registered successfully.");
            } else {
                System.out.println("Failed to register for the course.");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error while registering for course: " + e.getMessage(), e);
        }
    }

    public static void main(String[] args) {
        // Ensure database driver is loaded
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null,
                "MySQL JDBC Driver not found!",
                "Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Run the application
        SwingUtilities.invokeLater(() -> {
            new StudentCourseManagementSystem().setVisible(true);
        });
    }
}
