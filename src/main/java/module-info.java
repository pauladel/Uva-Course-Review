module CourseReviewGUI.main {
    requires java.sql;
    requires org.junit.jupiter.api;
    requires javafx.graphics;
    requires javafx.fxml;
    requires javafx.controls;
    opens edu.virginia.cs.gui to javafx.fxml;
    exports edu.virginia.cs.gui;
}