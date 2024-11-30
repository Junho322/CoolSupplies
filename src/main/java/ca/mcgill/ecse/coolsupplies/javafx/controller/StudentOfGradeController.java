package ca.mcgill.ecse.coolsupplies.javafx.controller;

import ca.mcgill.ecse.coolsupplies.controller.TOGrade;
import ca.mcgill.ecse.coolsupplies.controller.TOParent;
import ca.mcgill.ecse.coolsupplies.controller.TOStudent;
import ca.mcgill.ecse.coolsupplies.javafx.controller.GradePageController.EventListener;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

public class StudentOfGradeController {

    @FXML
    private Label gradeLabel;

    @FXML
    private Label studentLabel;

    @FXML
    void click(MouseEvent event) {

    }

    private TOStudent student;
    private EventListener listener;

    public void setStudent(TOStudent student, EventListener listener) {
        this.student = student;
        this.listener = listener;
        studentLabel.setText(student.getName());
        gradeLabel.setText("Grade " + student.getGradeLevel());
    }

    public void setSize(double distance) {
        gradeLabel.setTranslateX(distance);
    }
}
