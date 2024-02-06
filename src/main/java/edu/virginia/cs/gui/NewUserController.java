package edu.virginia.cs.gui;

import edu.virginia.cs.DataBaseCreation;
import edu.virginia.cs.Student;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class NewUserController {
    @FXML
    private Label username;
    @FXML
    private Label error;
    @FXML
    private TextField user;
    @FXML
    private PasswordField newPass;
    @FXML
    private PasswordField confirmPass;
    @FXML
    private Button back;

    public void Cursor(KeyEvent event) {

    }

    @FXML
    public void back(ActionEvent e) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(CourseReviewApplication.class.getResource("login-view.fxml"));
        Stage stage = (Stage)((Node)e.getSource()).getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load(), 400, 600);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void logIn(ActionEvent e) throws IOException {
        DataBaseCreation manager = new DataBaseCreation();
        manager.initializeDatabase();
        if (newPass.getText().equals(confirmPass.getText())) {
            Student newStudent = new Student(user.getText(), newPass.getText());
            try {
                DataBaseCreation.addStudentToTable(newStudent);
            }
            catch(IllegalArgumentException l){
                error.setText("Username is taken. Please try again.");
                manager.disconnect();
                return;
            }
            Stage current = (Stage)((Node)e.getSource()).getScene().getWindow();
            current.setUserData(DataBaseCreation.studentID(user.getText()));
            ExistingUserController.goMain(e);
        }
        else{
            //go to login
            back(e);

        }
        manager.disconnect();
    }




}
