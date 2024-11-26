package ca.mcgill.ecse.coolsupplies.javafx.pages;

import ca.mcgill.ecse.coolsupplies.controller.TOOrder;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

public class OrderCardController {

    @FXML
    private Label dateLabel;

    @FXML
    private Label orderLabel;

    public void setSize(double distance) {
        dateLabel.setTranslateX(distance);
    }

    @FXML
    void click(MouseEvent event) {

    }

    public void setOrder(TOOrder order) {
        dateLabel.setText(order.getDate().toString());
        orderLabel.setText("Order #" + Integer.toString(order.getNumber()));
    }
}
