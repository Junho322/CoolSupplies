package ca.mcgill.ecse.coolsupplies.javafx.pages;

import ca.mcgill.ecse.coolsupplies.controller.TOOrder;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;


/**
 * Controller class that handles operations related to the OrderCard in the system.
 *
 * @author Jack McDonald
 */
public class OrderCardController {

    @FXML
    private Label dateLabel;

    @FXML
    private Label orderLabel;

    /**
     * Sets the size of the OrderCard
     *
     * @param distance The distance to set the size to
     * @author Jack McDonald
     */
    public void setSize(double distance) {
        dateLabel.setTranslateX(distance);
    }

    /**
     * Handles the click event on the OrderCard
     *
     * @param event The MouseEvent that triggered the event
     * @author Jack McDonald
     */
    @FXML
    void click(MouseEvent event) {

    }

    /**
     * Sets the order of the OrderCard
     *
     * @param order The order to set the OrderCard to
     * @author Jack McDonald
     */
    public void setOrder(TOOrder order) {
        dateLabel.setText(order.getDate().toString());
        orderLabel.setText("Order #" + order.getNumber());
    }
}
