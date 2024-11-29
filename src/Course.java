/*
import java.awt.*;
import java.sql.*;
import java.util.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

// Custom Rounded Button Class
class RoundedButton extends JButton {
    public RoundedButton(String text) {
        super(text);
        setContentAreaFilled(false);
        setFocusPainted(false);
        setFont(new Font("Segoe UI", Font.BOLD, 14));
        setForeground(Color.WHITE);
        setBorder(new LineBorder(new Color(50, 150, 255), 2, true));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        GradientPaint gradient = new GradientPaint(0, 0, new Color(50, 150, 255), getWidth(), getHeight(), new Color(0, 255, 255));
        g2.setPaint(gradient);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
        super.paintComponent(g);
    }
}

// Student Class
class Student {
    private String id;
    private String name;

    public Student(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}

// Course Class
class Course {
    private String courseId;
    private String courseName;
    private String syllabus;
    private String faculty;
    private int credits;
    private List<Student> enrolledStudents;

    public Course(String courseId, String courseName, String syllabus, String faculty, int credits) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.syllabus = syllabus;
        this.faculty = faculty;
        this.credits = credits;
        this.enrolledStudents = new ArrayList<>();
    }

    public String getCourseId() {
        return courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getSyllabus() {
        return syllabus;
    }

    public String getFaculty() {
        return faculty;
    }

    public int getCredits() {
        return credits;
    }

    public List<Student> getEnrolledStudents() {
        return enrolledStudents;
    }

    public void enrollStudent(Student student) {
        enrolledStudents.add(student);
    }
}

// CourseManager Class
class CourseManager {
    private Map<String, Course> courses = new HashMap<>();
    private Connection connection;

    public CourseManager() {
        try {
            // Set up JDBC connection
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/student_course_db", "root", "23e51a6610");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public void addCourse(Course course) {
        courses.put(course.getCourseId(), course);
        // Save course to database
        saveCourseToDB(course);
    }

    public void registerStudent(String courseId, Student student) {
        Course course = courses.get(courseId);
        if (course != null) {
            course.enrollStudent(student);
            // Save student enrollment to database
            saveStudentEnrollment(courseId, student);
        }
    }

    public List<Course> getCourses() {
        return new ArrayList<>(courses.values());
    }

    public List<Student> getEnrolledStudentsFromDB(String courseId) {
        List<Student> enrolledStudents = new ArrayList<>();
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM student_enrollments WHERE courseId = ?");
            statement.setString(1, courseId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String studentId = resultSet.getString("studentId");
                String studentName = resultSet.getString("studentName");
                enrolledStudents.add(new Student(studentId, studentName));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return enrolledStudents;
    }

    public Course getCourseDetails(String courseId) {
        return courses.get(courseId);
    }

    private void saveCourseToDB(Course course) {
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO courses (courseId, courseName, syllabus, faculty, credits) VALUES (?, ?, ?, ?, ?)");
            statement.setString(1, course.getCourseId());
            statement.setString(2, course.getCourseName());
            statement.setString(3, course.getSyllabus());
            statement.setString(4, course.getFaculty());
            statement.setInt(5, course.getCredits());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void saveStudentEnrollment(String courseId, Student student) {
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO student_enrollments (courseId, studentId, studentName) VALUES (?, ?, ?)");
            statement.setString(1, courseId);
            statement.setString(2, student.getId());
            statement.setString(3, student.getName());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

// Main Application Class
public class StudentCourseManagementSystem extends JFrame {
    private CourseManager courseManager = new CourseManager();
    private JTextArea courseDetailsTextArea = new JTextArea();
    private JTable enrolledStudentsTable;
    private DefaultTableModel enrolledStudentsTableModel;
    private JComboBox<String> courseComboBox = new JComboBox<>();

    public StudentCourseManagementSystem() {
        // Adding new courses with detailed syllabus
        courseManager.addCourse(new Course("JAVA", "Java Programming",
            "\nMODULE- I\nHistory and Evolution of java: Java's lineage, Java and internet, Byte code, Java buzzwords,\n" +
            "Evolution of java.\nObject oriented programming - data, types, variables, Arrays, operators, control statements, type conversion\n" +
            "and casting, Introduction to classes, objects, methods, constructor, this and static keywords, garbage\n" +
            "collection, overloading methods, parameter passing, access control, Command line arguments, exploring\n" +
            "String class\nInheriting: member access and inheritance, Multilevel Inheritance, super and final keywords, method\n" +
            "overriding, dynamic method dispatch, abstract classes and methods.\nMODULE-II\nPackages and Interfaces: Defining, Creating and Accessing a Package, understanding CLASSPATH, \r\n" + //
                                "importing packages, Differences between classes and interfaces, defining an interface, implementing \r\n" + //
                                "interface, applying interfaces, variables in interface and extending interfaces. \r\n" + //
                                "Exception handling: Concepts of exception handling and its benefits, usage of try, catch, throw, throws and \r\n" + //
                                "finally, built in exceptions, creating own exceptions.\nMODULE-III\nMultithreading: Differences between multi-threading and multi programming, thread life cycle, creating \r\n" + //
                                                                        "threads using thread class and Runnable interface, thread priorities, synchronization , interthread \r\n" + //
                                                                        "communication.  \r\n" + //
                                                                        "I/O Streams: Stream classes, Byte and character streams, File class, reading and writing files, reading and \r\n" + //
                                                                        "writing from console, serialization.\n MODULE-IV\nApplets: Concepts of Applets, differences between applets and applications, life cycle of an applet, \r\n" + //
                                                                                                                                                        "creating applets, passing parameters to applets.  \r\n" + //
                                                                                                                                                        "AWT: class hierarchy, user interface components- labels, buttons, scrollbars, text components, checkbox, \r\n" + //
                                                                                                                                                        "checkbox groups, choices, lists panels – scroll pane, dialogs, menu bar, Layout Managers- Flow Layout, \r\n" + //
                                                                                                                                                        "Border Layout, Grid Layout, Card Layout, Grid Bag Layout. \r\n" + //
                                                                                                                                                        "Event Handling: Events, Event sources, Event classes, Event Listeners, Delegation event model, handling \r\n" + //
                                                                                                                                                        "mouse and keyboard events, Adapter classes.\nMODULE\nSwings: Introduction, limitations of AWT, MVC architecture, components, containers, exploring swing- J \r\n" + //
                                                                                                                                                                                                                                                                                                                        "Applet, J Frame and J Component, Image Icon, J Label, J Text field, J Button, J Checkbox, J List, J Radio \r\n" + //
                                                                                                                                                                                                                                                                                                                        "button, J Combo Box, J Tabbed Pane, J Scroll Pane. \r\n" + //
                                                                                                                                                                                                                                                                                                                        "The Collections Framework (java.util)- Collections overview, Collection Interfaces, Generics The \r\n" + //
                                                                                                                                                                                                                                                                                                                        "Collection classes- Array List, Linked List, Hash Set, Tree Set, Priority Queue, Array Deque. Accessing a \r\n" + //
                                                                                                                                                                                                                                                                                                                        "Collection via an iterator, Using an Iterator, The For-Each alternative, Map Interfaces and Classes, \r\n" + //
                                                                                                                                                                                                                                                                                                                        "Comparators, Collection algorithms, Arrays, The Legacy Classes and Interfaces- Dictionary, Hash table, \r\n" + //
                                                                                                                                                                                                                                                                                                                        "Properties, Stack, Vector More Utility classes, String Tokenizer, Date, Calendar, Random, Scanner.",
            "Rajeshwar sir", 5));

        courseManager.addCourse(new Course("DBMS", "Database Management", "\nMODULE-I\nDatabase System Applications: A Historical Perspective, File Systems versus a RDBMS, the Data Model, \r\n" + //
                        "Levels of Abstraction in a RDBMS, Data Independence, Structure of a RDBMS  \r\n" + //
                        "Introduction to Database Design: Database Design and ER Diagrams, Entities, Attributes, and Entity Sets, \r\n" + //
                        "Relationships and Relationship Sets, Super key, candidate key, Participating constraints, Weak entity, \r\n" + //
                        "Additional Features of the ER Model, Conceptual Design with the ER Model.\nMODULE-II\nSQL: Introduction To SQl , Query Languages, Basic SQL Query. Introduction to views, destroying/altering \r\n" + //
                                                        "tables and views. Joins. \r\n" + //
                                                        "Relational Algebra and Calculus: Selection and Projection, Set operations, Joins, Division, More examples \r\n" + //
                                                        "on Algebra queries, Tuple relational Calculus, Domain Relational Calculus.  \n MODULE-III\n Advanced SQL:  SQL Functions, Aggregate Operators, Group by & having clause, Sub queries, Nested \r\n" + //
                                                                                                                        "Queries, triggers and active data bases, cursors, procedures. \r\n" + //
                                                                                                                        "Schema Refinement: Problems caused by redundancy, decompositions, problems related to decomposition, \r\n" + //
                                                                                                                        "reasoning about functional dependencies, 1NF, 2NF, 3NF, 3.5NF, lossless join decomposition, multi-valued \r\n" + //
                                                                                                                        "dependencies, 4NF & 5NF.\n MODULE-IV\nTransaction Management: The ACID Properties, Transactions and Schedules, Concurrent Execution of \r\n" + //
                                                                                                                                                                                                                                                        "Transactions, 2PL, Serializability, Time stamp based protocol, validation based Protocol. Implementation of  \r\n" + //
                                                                                                                                                                                                                                                        "isolation, Multiple granularity,  \r\n" + //
                                                                                                                                                                                                                                                        "Recoverability: Recoverability, Introduction to Lock Management, Lock Conversions, Dealing with \r\n" + //
                                                                                                                                                                                                                                                        "Deadlocks, shadow paging. \n MODULE-V\nModule -V \r\n" + //
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        "Storage and Indexing: Data on External Storage, File Organization and Indexing, Cluster Indexes, Primary \r\n" + //
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        "and Secondary Indexes, Index data Structures, Hash Based Indexing, Tree base Indexing. \r\n" + //
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        "Tree Structured Indexing: Intuitions for tree Indexes, Indexed Sequential Access Methods (ISAM), B+ \r\n" + //
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        "Trees. ", "Surekha maam", 4));


        courseManager.addCourse(new Course("SMF", "Statistical Methods for Finance", "\n MODULE-I\nProbability: Sample Space, Events, Counting Sample Points, Probability of an Event, Addition Theorem \r\n" + //
                        "Conditional Probability, Independence, Multiplications theorem and Bayes’ theorem.  \r\n" + //
                        "Random Variables: Discrete and Continuous random variable. Definitions of Probability Distributions, \r\n" + //
                        "Probability Mass function, Probability Density function. Definitions of Mathematical expectation, Variance \r\n" + //
                        "Moment generating function of Discrete and continuous random variables.\nMODULE-II\nDiscrete Probability Distributions: Binomial, Poisson distribution and statistical constants of these \r\n" + //
                                                        "distributions using moment generating function. \r\n" + //
                                                        "Continuous Probability Distributions Uniform Distribution, Exponential Distribution and statistical \r\n" + //
                                                        "constants of these distributions using moment generating function. Normal Distribution and its related \r\n" + //
                                                        "applications.\n MODULE-III\nSampling Distribution: Random Sampling, Some Important Statistics, Sampling Distributions, Sampling \r\n" + //
                                                                                                                        "Distribution of Means, variance and the Central Limit Theorem. \r\n" + //
                                                                                                                        "Estimation and Tests of Significance: Introduction, Statistical Inference, Classical Methods of Estimation.: \r\n" + //
                                                                                                                        "Estimating the Mean, Standard Error of a Point Estimate, Null & Alternative Hypothesis, Critical region, \r\n" + //
                                                                                                                        "Type I and Type II errors, level of significance, one tail, two-tail tests. Prediction Intervals: Estimating a Mean \r\n" + //
                                                                                                                        "and Proportion for single sample, Difference between Two Means, difference between two proportions for \r\n" + //
                                                                                                                        "two Samples. Tests of significance for large sample: test for single mean, difference of means, single \r\n" + //
                                                                                                                        "proportion, difference of proportions.\nMODULE-IV\nBivariate Distribution: Joint Probability distributions - Joint Probability mass function, joint probability \r\n" + //
                                                                                                                                                                                                                                                        "density function, Marginal Distribution, Covariance of two random variables. \r\n" + //
                                                                                                                                                                                                                                                        "Correlation and Regression: Karl Pearson coefficient of correlation, Rank correlation, Regression \r\n" + //
                                                                                                                                                                                                                                                        "coefficient, Lines of regression.\nMODULE-V\nGreatest Common Divisors and Prime Factorization: Greatest common divisors, The Euclidean algorithm, \r\n" + //
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        "The fundamental theorem of arithmetic, Factorization of integers and the Fermat numbers. \r\n" + //
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        "Congruences: Introduction to congruences, Linear congruences, The Chinese remainder theorem, Systems of \r\n" + //
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        "linear congruences.", "Dr. Kumar", 3));

        
        courseManager.addCourse(new Course("AI", "Artificial Intelligence", "\nMODULE-I\nIntroduction: Artificial Intelligence, AI Problems, AI Techniques, the Level of the Model, Criteria for Success. Problem \r\n" + //
                        "Space and Search, Defining the Problem as a State Space Search, Problem Characteristics; Tic-Tac-Toe Problem, \r\n" + //
                        "Production Systems. \r\n" + //
                        "Basic Search Techniques: Solving Problems by searching; Issues in The Design of Search Programs; Uniform search \r\n" + //
                        "strategies; Breadth first search, depth first search, depth limited search, bidirectional search, Best First search, \r\n" + //
                        "comparing search strategies in terms of complexity. \nMODULE-II\nSpecial Search Techniques: Heuristic Search, greedy best first search, A* search Problem Reduction, AO*Algorithm; \r\n" + //
                                                        "Hill climbing search, Simulated Annealing search; Genetic Algorithm; Constraint Satisfaction Problems; Adversarial \r\n" + //
                                                        "search, Games, Optimal decisions and strategies in games, Minimax search, Alpha, beta pruning.  \r\n" + //
                                                        "Knowledge Representation: Procedural Vs Declarative Knowledge, Representations & Approaches to Knowledge \r\n" + //
                                                        "Representation, Forward Vs Backward Reasoning, Matching Techniques, Partial Matching, Fuzzy Matching Algorithms \r\n" + //
                                                        "and RETE Matching Algorithms.\nMODULE-III\nSymbolic Logic: Propositional Logic, First Order Predicate Logic: Representing Instance and is-a Relationships, \r\n" + //
                                                                                                                        "Computable Functions and Predicates, Syntax & Semantics of FOPL, Normal Forms, Unification &Resolution, \r\n" + //
                                                                                                                        "Representation Using Rules, Natural Deduction. \r\n" + //
                                                                                                                        "Structured Representations of Knowledge: Semantic Nets, Partitioned Semantic Nets, Frames, Conceptual \r\n" + //
                                                                                                                        "Dependency, Conceptual Graphs, Scripts, CYC.\nMODULE-IV\nReasoning under Uncertainty: Introduction to Non-Monotonic Reasoning, Truth Maintenance Systems, Logics for \r\n" + //
                                                                                                                                                                                                                                                        "Non-Monotonic Reasoning, Model and Temporal Logics. \r\n" + //
                                                                                                                                                                                                                                                        "Statistical Reasoning: Bayes Theorem, Certainty Factors and Rule-Based Systems, Bayesian Probabilistic Inference, \r\n" + //
                                                                                                                                                                                                                                                        "Bayesian Networks, Dempster-Shafer Theory. \r\n" + //
                                                                                                                                                                                                                                                        "Fuzzy Logic: Crisp Sets, Fuzzy Sets, Fuzzy Logic Control, Fuzzy Inferences & Fuzzy Systems.\nMODULE-V\nExperts Systems: Overview of an Expert System, Structure of an Expert Systems, Different Types of Expert Systems- \r\n" + //
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        "Rule Based, Model Based, Case Based and Hybrid Expert Systems, Knowledge Acquisition and Validation Techniques, \r\n" + //
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        "Black Board Architecture, Knowledge Building System Tools, Expert System Shells, Fuzzy Expert systems.  \r\n" + //
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        "Learning: Types of learning, general learning model, Learning by induction; generalization, specialization, example of \r\n" + //
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        "inductive learner.", "Prof. Ramesh", 4));

        setTitle("Student Course Management System");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        JTabbedPane tabbedPane = new JTabbedPane();

        tabbedPane.addTab("Register", createRegistrationPanel());
        tabbedPane.addTab("Course Information", createCourseInfoPanel());
        tabbedPane.addTab("Enrolled Students", createEnrolledStudentsPanel());

        contentPanel.add(tabbedPane, BorderLayout.CENTER);
        add(contentPanel, BorderLayout.CENTER);
        setVisible(true);
    }

    private JPanel createRegistrationPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(240, 248, 255)); // Light Blue background
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel courseLabel = new JLabel("Select Course:");
        courseLabel.setFont(new Font("Arial", Font.BOLD, 16));

        courseComboBox = new JComboBox<>();
        for (Course course : courseManager.getCourses()) {
            courseComboBox.addItem(course.getCourseId());
        }
        courseComboBox.setFont(new Font("Arial", Font.PLAIN, 14));
        courseComboBox.setBackground(new Color(255, 255, 255));
        courseComboBox.setForeground(new Color(50, 50, 50));

        JLabel studentIdLabel = new JLabel("Student ID:");
        studentIdLabel.setFont(new Font("Arial", Font.BOLD, 16));

        JTextField studentIdField = new JTextField(15);
        studentIdField.setFont(new Font("Arial", Font.PLAIN, 14));
        studentIdField.setBackground(new Color(255, 255, 255));

        JLabel studentNameLabel = new JLabel("Student Name:");
        studentNameLabel.setFont(new Font("Arial", Font.BOLD, 16));

        JTextField studentNameField = new JTextField(15);
        studentNameField.setFont(new Font("Arial", Font.PLAIN, 14));
        studentNameField.setBackground(new Color(255, 255, 255));

        RoundedButton registerButton = new RoundedButton("Register");
        registerButton.setBackground(new Color(50, 150, 255));
        registerButton.addActionListener(e -> {
            String courseId = (String) courseComboBox.getSelectedItem();
            String studentId = studentIdField.getText();
            String studentName = studentNameField.getText();
            if (!studentId.isEmpty() && !studentName.isEmpty()) {
                Student student = new Student(studentId, studentName);
                courseManager.registerStudent(courseId, student);
                JOptionPane.showMessageDialog(this, "Student Registered Successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "Please fill in all fields.");
            }
        });

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(courseLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        panel.add(courseComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(studentIdLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(studentIdField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(studentNameLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        panel.add(studentNameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        panel.add(registerButton, gbc);

        return panel;
    }

    private JPanel createCourseInfoPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(240, 248, 255));

        JTextArea courseDetailsTextArea = new JTextArea();
        courseDetailsTextArea.setEditable(false);
        courseDetailsTextArea.setFont(new Font("Arial", Font.PLAIN, 14));
        courseDetailsTextArea.setBorder(new LineBorder(new Color(50, 150, 255), 2, true));
        courseDetailsTextArea.setBackground(Color.WHITE);
        JScrollPane scrollPane = new JScrollPane(courseDetailsTextArea);
        panel.add(scrollPane, BorderLayout.CENTER);

        JComboBox<String> courseIdComboBox = new JComboBox<>();
        for (Course course : courseManager.getCourses()) {
            courseIdComboBox.addItem(course.getCourseId());
        }
        panel.add(courseIdComboBox, BorderLayout.NORTH);

        courseIdComboBox.addActionListener(e -> {
            String courseId = (String) courseIdComboBox.getSelectedItem();
            Course course = courseManager.getCourseDetails(courseId);
            if (course != null) {
                courseDetailsTextArea.setText("Course ID: " + course.getCourseId() +
                        "\nCourse Name: " + course.getCourseName() +
                        "\nSyllabus: " + course.getSyllabus() +
                        "\nFaculty: " + course.getFaculty() +
                        "\nCredits: " + course.getCredits());
            }
        });

        return panel;
    }

    private JPanel createEnrolledStudentsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(240, 248, 255));

        enrolledStudentsTableModel = new DefaultTableModel(new Object[]{"Student ID", "Student Name"}, 0);
        enrolledStudentsTable = new JTable(enrolledStudentsTableModel);

        JScrollPane scrollPane = new JScrollPane(enrolledStudentsTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        JComboBox<String> courseComboBox = new JComboBox<>();
        for (Course course : courseManager.getCourses()) {
            courseComboBox.addItem(course.getCourseId());
        }
        panel.add(courseComboBox, BorderLayout.NORTH);

        courseComboBox.addActionListener(e -> {
            String courseId = (String) courseComboBox.getSelectedItem();
            List<Student> enrolledStudents = courseManager.getEnrolledStudentsFromDB(courseId);
            enrolledStudentsTableModel.setRowCount(0); // Clear existing data
            for (Student student : enrolledStudents) {
                enrolledStudentsTableModel.addRow(new Object[]{student.getId(), student.getName()});
            }
        });

        return panel;
    }

    public static void main(String[] args) {
        new StudentCourseManagementSystem();
    }
}



 1. Type-1: JDBC-ODBC Bridge Driver
Description:
Acts as a bridge between Java applications and ODBC (Open Database Connectivity).
Translates JDBC method calls into ODBC function calls.
Advantages:
Easy to set up.
Useful for legacy systems that support ODBC.
Disadvantages:
Requires ODBC drivers installed on the client machine.
Platform-dependent (relies on native libraries).
Slower compared to other driver types.
Status: Deprecated in Java 8 and removed in Java 9.
2. Type-2: Native-API Driver
Description:
Uses native database client APIs (specific to a database, like Oracle or MySQL) to communicate with the database.
Converts JDBC calls to native database API calls.
Advantages:
Faster than Type-1 as it bypasses ODBC.
Can use advanced database features via native API.
Disadvantages:
Requires native library installation for each database.
Platform-dependent and less portable.
3. Type-3: Network Protocol Driver
Description:
Uses a middleware server that converts JDBC calls to the database-specific protocol.
The Java application communicates with the middleware using a database-independent protocol.
Advantages:
Fully portable (no need for native libraries).
Centralized access management.
Disadvantages:
Middleware server adds complexity and requires maintenance.
Can introduce latency due to additional network hop.
4. Type-4: Thin Driver
Description:
Directly converts JDBC calls into the database's native protocol (e.g., MySQL, Oracle).
No intermediate layers like ODBC or middleware.
Advantages:
Platform-independent and fully written in Java.
High performance as it communicates directly with the database.
Does not require native libraries or middleware.
Disadvantages:
Driver must be specific to the database (e.g., MySQL Connector/J, Oracle Thin Driver).
 */

