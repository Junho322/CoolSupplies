package ca.mcgill.ecse.coolsupplies.javafx.controller;
import ca.mcgill.ecse.coolsupplies.controller.TOGrade;
import ca.mcgill.ecse.coolsupplies.javafx.controller.GradePageController.EventListener;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

public class GradeController {

    @FXML
    private Label gradeLabel;

    @FXML
    private Label studentLabel;

    @FXML
    private Label bundleLabel;

    @FXML
    private void click(MouseEvent event) {
        listener.onClickListener(grade);
    }

    private TOGrade grade;
    private EventListener listener;

    public void setGrade(TOGrade grade, int students, String bundleName, EventListener listener) {
        this.grade = grade;
        this.listener = listener;
        gradeLabel.setText(grade.getLevel());
        if (students != 1) {
            studentLabel.setText(students + " students");
        }
        else {
            studentLabel.setText(students + " student");
        }
        if (bundleName != null) {
            bundleLabel.setText("Bundle: " + bundleName);
        }
        else {
            bundleLabel.setText("");
        }
    }
}
