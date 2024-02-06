package edu.virginia.cs.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {
    @FXML
    private Label welcome;
    @FXML
    private Label login;
    @FXML
    private Button existing;
    @FXML
    private Button newuser;

    public void Cursor(KeyEvent e) {

    }

    public void SwitchtoExisting(ActionEvent e) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(CourseReviewApplication.class.getResource("existinguser-view.fxml"));
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load(), 400, 600);
        stage.setScene(scene);
        stage.show();

        //Parent root = new FXMLLoader(CourseReviewApplication.class.getResource("login-view.fxml"));
        //Parent root = FXMLLoader.load(CourseReviewApplication.class.getResource(newuser-view.fxml));


//        FXMLLoader fxmlLoader = new FXMLLoader(CourseReviewApplication.class.getResource("login-view.fxml"));
//        Scene scene = new Scene(fxmlLoader.load(), 400, 600);
//        stage.setTitle("Course Review");
//        stage.setScene(scene);
//        stage.show();

    }

    public void SwitchtoNew(ActionEvent e) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(CourseReviewApplication.class.getResource("newuser-view.fxml"));
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load(), 400, 600);
        stage.setScene(scene);
        stage.show();
    }
}