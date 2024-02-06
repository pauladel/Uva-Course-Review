package edu.virginia.cs.gui;

import edu.virginia.cs.Course;
import edu.virginia.cs.DataBaseCreation;
import edu.virginia.cs.Review;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;

public class SubmitReviewController {
    @FXML
    private TextField courseName;
    @FXML
    private TextArea message;
    @FXML
    private TextField rating;
    @FXML
    private Label error;
    @FXML
    private Button submit;
    @FXML
    private Button back;
    @FXML
    public void back(ActionEvent e){
        try{
            ExistingUserController.goMain(e);
        }
        catch(IOException l){
            l.printStackTrace();
        }
    }
    @FXML
    public void submission(ActionEvent e) throws IOException {
        DataBaseCreation manager = new DataBaseCreation();
        manager.initializeDatabase();
        String[] course = courseName.getText().split(" ");
        String dept = course[0];
        String num = course[1];
        if (!validCourse(dept, num)) {error.setText("Invalid Course Name");}
        else{
            String courseID = DataBaseCreation.courseID(num, dept);
            if (courseID == null){
                DataBaseCreation.addCourseToTable(new Course(dept, num));
                courseID = DataBaseCreation.courseID(num, dept);
            }
            Stage current = (Stage)((Node)e.getSource()).getScene().getWindow();
            String studentID = (String) current.getUserData();
            String reviewMessage = message.getText();
            int reviewRating = Integer.parseInt(rating.getText());
            try{
                if (DataBaseCreation.alreadyReviewed(courseID, studentID)) {
                    error.setText("Error: Only 1 review per class");
                    manager.disconnect();
                    return;
                }
                DataBaseCreation.addReviewtoTable(new Review(studentID, courseID, reviewMessage, reviewRating));
                error.setTextFill(Color.BLACK);
                error.setText("Review Submitted. Thanks!");
            }
            catch (IllegalArgumentException l){
                error.setText("Invalid Review. Try Again.");
            }

        }
        manager.disconnect();manager.disconnect();
    }

    private boolean validCourse(String dept, String num){
        boolean result = true;
        if (dept.length() > 4 || num.length() > 4){result= false;}
        for (int i = 0; i < dept.length(); i++){
            if (!Character.isUpperCase(dept.charAt(i))){result = false;}
        }
        return result;
    }
}
