package edu.virginia.cs.gui;

import edu.virginia.cs.DataBaseCreation;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;


public class CourseReviewApplication extends Application {
    private int studentID;

    public int getStudentID(){
        return studentID;
    }

    public void setStudentID(int ID){
        studentID = ID;
    }
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(CourseReviewApplication.class.getResource("login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 400, 600);
        stage.setTitle("Course Review");
        stage.setScene(scene);
        stage.show();

    }

    public static void main(String[] args) {
        DataBaseCreation init = new DataBaseCreation();
        init.initializeDatabase();
        init.disconnect();
        launch();
    }
}