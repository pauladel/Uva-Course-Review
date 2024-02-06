package edu.virginia.cs.gui;

import edu.virginia.cs.DataBaseCreation;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class ExistingUserController {
    @FXML
    private Label error;
    @FXML
    private TextField user;
    @FXML
    private TextField pass;

    @FXML
    private Button back;

    public void Cursor(KeyEvent event) {
        if (!event.getText().isBlank()) {
            user.setStyle("-fx-text-inner-color: #000000; -fx-background-color: #FFFFFF;");
            user.setEditable(true);
            pass.setStyle("-fx-text-inner-color: #000000; -fx-background-color: #FFFFFF;");
            pass.setEditable(true);
        }


    }
    @FXML
    public void back(ActionEvent e) throws IOException {
        //FXMLLoader fxmlLoader = new FXMLLoader(CourseReviewApplication.class.getResource("login-view.fxml"));
        FXMLLoader fxmlLoader = new FXMLLoader(CourseReviewApplication.class.getResource("login-view.fxml"));
        Stage stage = (Stage)((Node)e.getSource()).getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load(), 400, 600);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public static void goMain(ActionEvent e) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(CourseReviewApplication.class.getResource("mainmenu-view.fxml"));
        Stage stage = (Stage)((Node)e.getSource()).getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load(), 400, 600);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void logIn(ActionEvent e) throws IOException {
        DataBaseCreation manager = new DataBaseCreation();
        manager.initializeDatabase();
        try{
            if (DataBaseCreation.checkPasswordIsCorrect(user.getText(), pass.getText())) {
                Stage current = (Stage)((Node)e.getSource()).getScene().getWindow();
                current.setUserData(DataBaseCreation.studentID(user.getText()));
                goMain(e);
            }
            else{
                //flash error
                error.setText("We couldn't log you in! Please try again or create a new account.");


            }
        }
        catch (SQLException l){l.printStackTrace();}
        manager.disconnect();
    }

}
