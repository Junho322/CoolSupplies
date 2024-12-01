package ca.mcgill.ecse.coolsupplies.javafx.pages;

import ca.mcgill.ecse.coolsupplies.controller.TOParent;
import ca.mcgill.ecse.coolsupplies.javafx.controller.EventListenerParent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

/**
 * Controller class that handles operations related to the Parent in the system.
 *
 * @author Jack McDonald
 */
public class ParentController {

    @FXML
    private Label parentLabel;

    @FXML
    private Label studentLabel;

    private TOParent parent;
    private EventListenerParent listener;

    /**
     * Handles the click event on the ParentCard
     *
     * @param event The MouseEvent that triggered the event
     * @author Jack McDonald
     */
    @FXML
    private void click(MouseEvent event) {
        listener.onClickListener(parent);
    }

    /**
     * Sets the parent of the ParentCard
     *
     * @param parent   The parent to set the ParentCard to
     * @param students The number of students the parent has
     * @param listener The EventListenerParent to set the ParentCard to
     * @author Jack McDonald
     */
    public void setParent(TOParent parent, int students, EventListenerParent listener) {
        this.parent = parent;
        this.listener = listener;
        parentLabel.setText(parent.getName());
        if (students != 1) {
            studentLabel.setText(students + " students");
        } else {
            studentLabel.setText(students + " student");
        }
    }
}
