package ca.mcgill.ecse.coolsupplies.javafx.controller;

import ca.mcgill.ecse.coolsupplies.controller.CoolSuppliesFeatureSet8Controller;
import ca.mcgill.ecse.coolsupplies.controller.TOOrder;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

/**
 * handles update order functinality 
 * 
 * @author Shayan Yamanidouzi Sorkhabi
 */

public class UpdateOrderController {

    @FXML
    private TextField StudentName;

    @FXML
    private TextField newLevel;

    private static TOOrder Order;


    /**
     * sets the order that we want to be updated
     * @param o
     * @author Shayan Yamanidouzi Sorkhabi
     */
    public static void setExistingOrder(TOOrder o) {
        Order = o;
    }


    /**
     * retrieves the inputted value through the ui and handles updating an orde rthrough the backend controller method
     * @param event the event triggererd by clicking the update order button
     * @author Shayan Yamanidouzi Sorkhabi
     */

    @FXML
    void updateOrder(ActionEvent event) {
        String studentName = StudentName.getText().trim();
        String level = newLevel.getText().trim();

        if (studentName.isEmpty() || level.isEmpty()) {
            showAlert("Error", "Both Student Name and Purchase Level are required.");
            return;
        }


        int orderNumber = Order.getNumber(); //get Ordernumber that is given to me


        String result = CoolSuppliesFeatureSet8Controller.updateOrder(orderNumber, level, studentName);


        if (result.equals("Order updated successfully")) {
            showAlert("Success", "The order has been updated successfully.");
        } else {
            showAlert("Error", result);  
        }
    }

    /**
     * Helper method to display an alert to the user.
     * @param title The title of the alert
     * @param message The message to be displayed in the alert
     * @author Shayan Yamanidouzi Sorkhabi
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
