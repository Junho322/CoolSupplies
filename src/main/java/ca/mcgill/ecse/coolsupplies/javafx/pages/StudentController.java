package ca.mcgill.ecse.coolsupplies.javafx.pages;

import ca.mcgill.ecse.coolsupplies.controller.TOStudent;
import ca.mcgill.ecse.coolsupplies.javafx.controller.EventListenerParent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

/**
 * Controller class that handles operations related to the Student in the system.
 *
 * @author Jack McDonald
 */
public class StudentController {

    @FXML
    private Label gradeLabel;

    @FXML
    private Label studentLabel;

    private TOStudent student;
    private EventListenerParent listener;

    /**
     * Handles the click event on the StudentCard
     *
     * @param event The MouseEvent that triggered the event
     * @author Jack McDonald
     */
    @FXML
    void click(MouseEvent event) {
    }

    /**
     * Sets the student of the StudentCard
     *
     * @param student  The student to set the StudentCard to
     * @param listener The EventListenerParent to set the StudentCard to
     * @author Jack McDonald
     */
    public void setStudent(TOStudent student, EventListenerParent listener) {
        this.student = student;
        this.listener = listener;
        studentLabel.setText(student.getName());
        gradeLabel.setText("Grade " + student.getGradeLevel());
    }

    /**
     * Sets the size of the StudentCard
     *
     * @param distance The distance to set the size to
     * @author Jack McDonald
     */
    public void setSize(double distance) {
        gradeLabel.setTranslateX(distance);
    }
}
