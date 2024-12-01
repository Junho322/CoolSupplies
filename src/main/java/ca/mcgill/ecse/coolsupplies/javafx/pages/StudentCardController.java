package ca.mcgill.ecse.coolsupplies.javafx.pages;

import ca.mcgill.ecse.coolsupplies.controller.TOStudent;
import ca.mcgill.ecse.coolsupplies.javafx.controller.EventListenerStudent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

/**
 * Controller class that handles operations related to the StudentCard in the system.
 *
 * @author Jack McDonald
 */
public class StudentCardController {

    @FXML
    private Label gradeLabel;

    @FXML
    private Label studentLabel;

    private TOStudent student;
    private EventListenerStudent listener;

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
     * @param student    The student to set the StudentCard to
     * @param gradeLevel The grade level of the student
     * @param listener   The EventListenerStudent to set the StudentCard to
     * @author Jack McDonald
     */
    public void setStudent(TOStudent student, String gradeLevel, EventListenerStudent listener) {
        this.student = student;
        this.listener = listener;
        studentLabel.setText(student.getName());
        gradeLabel.setText("Grade " + gradeLevel);
    }
}
