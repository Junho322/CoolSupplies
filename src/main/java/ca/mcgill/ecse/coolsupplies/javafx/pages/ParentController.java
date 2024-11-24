package ca.mcgill.ecse.coolsupplies.javafx.pages;

import ca.mcgill.ecse.coolsupplies.controller.TOParent;
import ca.mcgill.ecse.coolsupplies.javafx.controller.EventListener;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

public class ParentController {

    @FXML
    private Label parentLabel;

    @FXML
    private Label studentLabel;

    @FXML
    private void click(MouseEvent event) {
        listener.onClickListener(parent);
    }

    private TOParent parent;
    private EventListener listener;

    public void setParent(TOParent parent, int students, EventListener listener) {
        this.parent = parent;
        this.listener = listener;
        parentLabel.setText(parent.getName());
        if (students != 1) {
            studentLabel.setText(students + " students");
        }
        else {
            studentLabel.setText(students + " student");
        }
    }
}
