
/* 
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class StudentCourseDBSetup {
    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306", "root", "23e51a6610");

            // Check if the database exists; if not, create it
            Statement statement = connection.createStatement();
            statement.executeUpdate("CREATE DATABASE IF NOT EXISTS student_course_db");

            // Use the created database
            statement.executeUpdate("USE student_course_db");

            // Create the courses table if it doesn't exist
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS courses (" +
                    "courseId VARCHAR(50) PRIMARY KEY," +
                    "courseName VARCHAR(100) NOT NULL," +
                    "syllabus TEXT," +
                    "faculty VARCHAR(50)," +
                    "credits INT)");

            // Create the student_enrollments table if it doesn't exist
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS student_enrollments (" +
                    "courseId VARCHAR(50)," +
                    "studentId VARCHAR(50)," +
                    "studentName VARCHAR(100) NOT NULL," +
                    "PRIMARY KEY (courseId, studentId)," +
                    "FOREIGN KEY (courseId) REFERENCES courses(courseId))");

            // Optionally, you could create an index for studentId for faster lookups
            statement.executeUpdate("CREATE INDEX IF NOT EXISTS idx_studentId ON student_enrollments(studentId)");

            System.out.println("Database and tables created successfully!");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }
}

*/
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/student_course_db";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "23e51a6610";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    public static List<String[]> getAllCourses() {
        List<String[]> courses = new ArrayList<>();
        String query = "SELECT courseId, courseName FROM courses";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                courses.add(new String[]{rs.getString("courseId"), rs.getString("courseName")});
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching courses: " + e.getMessage(), e);
        }
        return courses;
    }

    public static String[] getCourseDetails(String courseId) {
        String query = "SELECT courseName, faculty, credits, syllabus FROM courses WHERE courseId = ?";
        String[] details = null;

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, courseId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    details = new String[]{
                            rs.getString("courseName"),
                            rs.getString("faculty"),
                            rs.getString("credits"),
                            rs.getString("syllabus")
                    };
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching course details: " + e.getMessage(), e);
        }
        return details;
    }

    public static List<String[]> getEnrolledStudents(String courseId) {
        List<String[]> students = new ArrayList<>();
        String query = "SELECT studentId, studentName FROM student_enrollments WHERE courseId = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, courseId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    students.add(new String[]{rs.getString("studentId"), rs.getString("studentName")});
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching enrolled students: " + e.getMessage(), e);
        }
        return students;
    }
}
