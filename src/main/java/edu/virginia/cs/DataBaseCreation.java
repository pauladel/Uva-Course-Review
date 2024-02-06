package edu.virginia.cs;

import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.ArrayList;
import java.text.DecimalFormat;

public class DataBaseCreation {
    static Connection connection;

    private static final DecimalFormat df = new DecimalFormat("0.00");
    public static void main(String[] args) throws SQLException {
        initializeDatabase();
        //connectDatabase();
        clearTables();
        createTables();
        Course course = new Course("CS", "1100");
        addCourseToTable(course);
        System.out.println(courseID("1100", "CS"));
        Student wyatt = new Student("wyatt", "123");
        Student dani = new Student("dani", "456");
        System.out.println(wyatt.getID());
        System.out.println(dani.getID());
        addStudentToTable(wyatt);
        addStudentToTable(dani);
        Review review = new Review("1", courseID("1100", "CS"), "good stuff!", 4);
        addReviewtoTable(review);
        Review review2 = new Review("9", courseID("1100", "CS"), "sucked balls", 1);
        Review review3 = new Review("10", courseID("1100", "CS"), "mid", 3);
        addReviewtoTable(review2);
        addReviewtoTable(review3);
        //System.out.println(getScoreForCourse(courseID("1100", "CS")));
        //printReviewsForCourse("3140");
        //printAverageReviewScoreForCourse("3140");

    }
    public static void initializeDatabase() {
        String databaseName = "src/Reviews.sqlite3";
        String databaseUrl = "jdbc:sqlite:" + databaseName;
        try {
            // Check if database file exists
            // Establish connection to database
            // Create tables if they don't exist
            connection = DriverManager.getConnection(databaseUrl);
            //createTables();
            //connection.close();
        }  catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public static void verifyConnection() {
        try {
            if(connection.isClosed()||connection==null) {
                throw new IllegalStateException("Manager not connected!");
            }
        } catch (SQLException e) {
            throw new RuntimeException();
        }
    }
    public static void clearTables() {
        verifyConnection();
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate("DELETE FROM REVIEWS");
            statement.executeUpdate("DELETE FROM STUDENTS");
            statement.executeUpdate("DELETE FROM COURSES");
        } catch(SQLException e){
            throw new RuntimeException(e);
        }
    }

//    public static void connectDatabase(){
//
//        String databaseName = "src/Reviews.sqlite3";
//        String url = "jdbc:sqlite:" + databaseName;
//        try{
//            Class.forName("org.sqlite.JDBC");
//            connection = DriverManager.getConnection(url);
//        }
//        catch (ClassNotFoundException | SQLException e) {
//            throw new RuntimeException(e);
//        }
//    }
    public static void createTables() {
        verifyConnection();
        try {
            Statement statement = connection.createStatement();

            String studentsTable = "CREATE TABLE IF NOT EXISTS STUDENTS (" +
                    "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "LOGIN VARCHAR(255) NOT NULL UNIQUE," +
                    "PASSWORD VARCHAR(255) NOT NULL" +
                    ");";

            String coursesTable = "CREATE TABLE IF NOT EXISTS COURSES (" +
                    "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "DEPARTMENT VARCHAR(255) NOT NULL," +
                    "CATALOG_NUMBER INT(4) NOT NULL" +
                    ");";

            String reviewsTable = "CREATE TABLE IF NOT EXISTS REVIEWS (" +
                    "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "STUDENTID INT NOT NULL," +
                    "COURSEID INT NOT NULL," +
                    "REVIEW_MESSAGE VARCHAR(300) NOT NULL," +
                    "RATING INT(1) NOT NULL CHECK(RATING >= 1 AND RATING <= 5)," +
                    "FOREIGN KEY (STUDENTID) REFERENCES STUDENTS(ID) ON DELETE CASCADE," +
                    "FOREIGN KEY (COURSEID) REFERENCES COURSES(ID) ON DELETE CASCADE" +
                    ");";

            statement.executeUpdate(studentsTable);
            statement.executeUpdate(coursesTable);
            statement.executeUpdate(reviewsTable);

            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean checkPasswordIsCorrect(String username, String password) throws SQLException {
        verifyConnection();
        try {
            PreparedStatement checkStmt = connection.prepareStatement("SELECT * FROM STUDENTS WHERE LOGIN = ?");
            checkStmt.setString(1, username);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next()) {
                if (password.equals(rs.getString("PASSWORD"))) {
                    return true;
                }
                return false;
            }
            return false;
        }catch(SQLException e){
            throw new RuntimeException(e);
        }
    }
    public static void addReviewtoTable(Review review) throws IllegalArgumentException{
        verifyConnection();
        try {
            Statement statement = connection.createStatement();
            String sql = "INSERT INTO REVIEWS (STUDENTID, COURSEID, REVIEW_MESSAGE, RATING) " +
                    "SELECT ?, ?, ?, ? " +
                    "WHERE NOT EXISTS " +
                    "(SELECT 1 FROM REVIEWS WHERE STUDENTID = ? AND COURSEID = ?);";
            PreparedStatement statement1 = connection.prepareStatement(sql);
            statement1.setString(1, review.getStudentID());
            statement1.setString(2, review.getCourseID());
            statement1.setString(3, review.getReview_message());
            statement1.setInt(4, review.getRating());
            statement1.setString(5, review.getStudentID());
            statement1.setString(6, review.getCourseID());
            statement1.executeUpdate();
        }catch(RuntimeException | SQLException e){
            throw new IllegalArgumentException();
        }



    }
    public static boolean courseInDatabase(int catalog, String dept){
        verifyConnection();
        try {
            PreparedStatement statement = connection.prepareStatement("select * from COURSES where DEPARTMENT = ? and CATALOG_NUMBER = ?");
            statement.setString(1, dept);
            statement.setInt(2, catalog);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return true;
            }
            return false;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public static void addCourseToTable(Course course){
        verifyConnection();
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO COURSES (DEPARTMENT, CATALOG_NUMBER) VALUES (?, ?)");
            //statement.setInt(1, course.getID());
            statement.setString(1, course.getDepartment());
            statement.setString(2, course.getCatalog());
            statement.executeUpdate();
        } catch(SQLException e){
            throw new RuntimeException(e);
        }
    }
    public static void addStudentToTable(Student student) throws IllegalArgumentException{
        verifyConnection();
        try {
            PreparedStatement checkStmt = connection.prepareStatement("SELECT * FROM STUDENTS WHERE LOGIN = ?");
            checkStmt.setString(1, student.getLogin());
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next()) {
                throw new IllegalArgumentException();
            }
            PreparedStatement insertStmt = connection.prepareStatement("INSERT INTO STUDENTS (LOGIN, PASSWORD) VALUES (?, ?)");
            //insertStmt.setInt(1, student.getID());
            insertStmt.setString(1, student.getLogin());
            insertStmt.setString(2, student.getPassword());
            insertStmt.executeUpdate();
            System.out.println("Student added to database.");
        } catch(SQLException e){
            throw new RuntimeException(e);
        }
    }
    public static boolean alreadyReviewed(String courseID, String studentID){
        verifyConnection();
        try {
            PreparedStatement checkStmt = connection.prepareStatement("SELECT * FROM REVIEWS WHERE STUDENTID = ?");
            checkStmt.setString(1, studentID);
            ResultSet rs = checkStmt.executeQuery();
            while (rs.next()) {
                if(rs.getString("courseID").equals(courseID)){
                    return true;
                }
            }
            return false;

        } catch(SQLException e){
            throw new RuntimeException(e);
        }
    }
    public static boolean courseAlreadyHasReviews(String catalog, String dept){
        verifyConnection();
        try {
            String x = courseID(catalog,dept);
            PreparedStatement checkStmt = connection.prepareStatement("SELECT * FROM REVIEWS WHERE COURSEID = ?");
            checkStmt.setString(1, x);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next()) {
                    return true;
            }
            return false;

        } catch(SQLException e){
            throw new RuntimeException(e);
        }
    }
    public static String courseID(String catalog, String dept){
        verifyConnection();
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM COURSES WHERE DEPARTMENT = ? and CATALOG_NUMBER = ?");
            statement.setString(1, dept);
            statement.setString(2, catalog);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                System.out.println("Course found");
                return rs.getString("ID");
            }
            return null;

        } catch(SQLException e){
            throw new RuntimeException(e);
        }
    }
    public static String studentID(String username){
        verifyConnection();
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM STUDENTS WHERE LOGIN = ?");
            statement.setString(1, username);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return rs.getString("ID");
            }
            return null;

        } catch(SQLException e){
            throw new RuntimeException(e);
        }
    }

    public static ArrayList<String> getReviewsForCourse(String courseID) {
        verifyConnection();
        ArrayList<String> reviews = new ArrayList<>();
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT REVIEW_MESSAGE FROM REVIEWS WHERE COURSEID = ?"
            );
            statement.setString(1, courseID);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                reviews.add(rs.getString("REVIEW_MESSAGE"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return reviews;
    }
    public static String printReviewsForCourse(String courseID) {
        verifyConnection();
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT REVIEW_MESSAGE FROM REVIEWS WHERE COURSEID = ?"
            );
            statement.setString(1, courseID);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                return rs.getString("REVIEW_MESSAGE");
                //System.out.println(rs.getString("REVIEW_MESSAGE"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
    public static String getAverageReviewScoreForCourse(String courseID) {
        verifyConnection();
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT AVG(RATING) AS AVERAGE_SCORE FROM REVIEWS WHERE COURSEID = ?"
            );
            statement.setString(1, courseID);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                double averageScore = rs.getDouble("AVERAGE_SCORE");
                if (averageScore == 0.0){return ("No reviews yet!");}
                return(df.format(averageScore) + "/5");
            } else {
                return ("No reviews yet!");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void disconnect() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


}
