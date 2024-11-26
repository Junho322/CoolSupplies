package ca.mcgill.ecse.coolsupplies.javafx.pages;

import ca.mcgill.ecse.coolsupplies.controller.TOStudent;
import ca.mcgill.ecse.coolsupplies.javafx.controller.EventListenerParent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

public class StudentController {

    @FXML
    private Label gradeLabel;

    @FXML
    private Label studentLabel;

    @FXML
    void click(MouseEvent event) {

    }

    private TOStudent student;
    private EventListenerParent listener;

    public void setStudent(TOStudent student, EventListenerParent listener) {
        this.student = student;
        this.listener = listener;
        studentLabel.setText(student.getName());
        gradeLabel.setText("Grade " + student.getGradeLevel());
    }

    public void setSize(double distance) {
        gradeLabel.setTranslateX(distance);
    }
}
