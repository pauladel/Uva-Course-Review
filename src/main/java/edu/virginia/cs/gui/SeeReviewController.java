package edu.virginia.cs.gui;

import edu.virginia.cs.DataBaseCreation;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.SQLException;

public class SeeReviewController {
    @FXML
    private TextField coursename;
    @FXML
    private Accordion reviews;
    @FXML
    private Button back;
    @FXML
    private Label error;
    @FXML
    private Label score;

    @FXML
    private ScrollPane reviewPane;

    @FXML
    public void back(ActionEvent e) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(CourseReviewApplication.class.getResource("mainmenu-view.fxml"));
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load(), 400, 600);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void courseSearch(ActionEvent e) throws IOException {
        DataBaseCreation manager = new DataBaseCreation();
        manager.initializeDatabase();
        score.setTextFill(Color.BLACK);
        String[] args = coursename.getText().split(" ");
        String id = DataBaseCreation.courseID(args[1], args[0]);

        if ((id) != null) {
            score.setText(DataBaseCreation.getAverageReviewScoreForCourse(id));
            VBox results = new VBox(5);

            for (String reviewMessage : DataBaseCreation.getReviewsForCourse(id)) {
                results.getChildren().add(new Label(reviewMessage));
            }
            reviewPane.setContent(results);
        } else {
            score.setTextFill(Color.RED);
            score.setText("Invalid course.");
        }
        manager.disconnect();
    }
}

