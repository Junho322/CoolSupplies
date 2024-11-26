package ca.mcgill.ecse.coolsupplies.javafx.pages;

import ca.mcgill.ecse.coolsupplies.controller.TOStudent;
import ca.mcgill.ecse.coolsupplies.javafx.controller.EventListenerStudent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

public class StudentCardController {

    @FXML
    private Label gradeLabel;

    @FXML
    private Label studentLabel;

    private TOStudent student;
    private EventListenerStudent listener;

    @FXML
    void click(MouseEvent event) {

    }

    public void setStudent(TOStudent student, String gradeLevel, EventListenerStudent listener) {
        this.student = student;
        this.listener = listener;
        studentLabel.setText(student.getName());
        gradeLabel.setText("Grade " + gradeLevel);
    }
}
