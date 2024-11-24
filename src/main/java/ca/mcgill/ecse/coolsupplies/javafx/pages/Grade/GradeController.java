package ca.mcgill.ecse.coolsupplies.javafx.pages.Grade;

import ca.mcgill.ecse.coolsupplies.controller.TOGrade;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

public class GradeController {

    @FXML
    private Label gradeLabel;

    @FXML
    private Label studentLabel;

    @FXML
    private void click(MouseEvent event) {
        listener.onClickListener(grade);
    }

    private TOGrade grade;
    private EventListener listener;

    public void setGrade(TOGrade grade, int students, EventListener listener) {
        this.grade = grade;
        this.listener = listener;
        gradeLabel.setText(grade.getLevel());
        if (students != 1) {
            studentLabel.setText(students + " students");
        }
        else {
            studentLabel.setText(students + " student");
        }
    }

    public interface EventListener {
      void onClickListener(TOGrade grade);
    }
}
