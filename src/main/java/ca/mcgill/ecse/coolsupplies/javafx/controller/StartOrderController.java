package ca.mcgill.ecse.coolsupplies.javafx.controller;

import ca.mcgill.ecse.coolsupplies.controller.CoolSuppliesFeatureSet6Controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

/**
 * handles start order functinality 
 * 
 * @author Shayan Yamanidouzi Sorkhabi
 */

public class StartOrderController {

    @FXML
    private DatePicker Date;

    @FXML
    private TextField Level;

    @FXML
    private TextField OrderNumber;

    @FXML
    private TextField ParentEmail;

    @FXML
    private TextField StudentName;

    /**
     * retrieves input values, validates them and calls the backened controller to create and order
     * If an order is succesffully created a message is shown
     * 
     * @param event triggered by clicking Start Order button
     * @author Shayan Yamanidouzi Sorkhabi
     */

    @FXML
    void startOrder(ActionEvent event) {
      // Retrieve values from UI fields
      String orderNumberText = OrderNumber.getText();
      String levelText = Level.getText();
      String parentEmailText = ParentEmail.getText();
      String studentNameText = StudentName.getText();
  
      if (Date.getValue() == null) {
          showAlert(AlertType.ERROR, "Input Error", "Date is required.");
          return;
      }
  
      java.sql.Date orderDate = java.sql.Date.valueOf(Date.getValue()); // Convert LocalDate to java.sql.Date
  
      // Validate inputs
      if (orderNumberText == null || orderNumberText.isEmpty() ||
          levelText == null || levelText.isEmpty() ||
          parentEmailText == null || parentEmailText.isEmpty() ||
          studentNameText == null || studentNameText.isEmpty()) {
  
          showAlert(AlertType.ERROR, "Input Error", "All fields are required.");
          return;
      }
  
      int orderNumber;
      try {
          orderNumber = Integer.parseInt(orderNumberText);
      } catch (NumberFormatException e) {
          showAlert(AlertType.ERROR, "Input Error", "Order number must be a valid integer.");
          return;
      }
  
      // Call controller method
      String result = CoolSuppliesFeatureSet6Controller.startOrder(orderNumber, orderDate, levelText, parentEmailText, studentNameText);
  
      // Display result
      if (result.equals("Order created successfully.")) {
          showAlert(AlertType.INFORMATION, "Success", result);
          clearFields();
      } else {
          showAlert(AlertType.ERROR, "Error", result);
      }
  }
  

    /**
     * Clears all the input fields in the form.
     * @author Shayan Yamanidouzi Sorkhabi
     */
    private void clearFields() {
        OrderNumber.clear();
        Date.setValue(null);
        Level.clear();
        ParentEmail.clear();
        StudentName.clear();
    }

    /**
     * Utility method to show an alert to the user.
     *
     * @param alertType The type of alert (e.g., ERROR, INFORMATION).
     * @param title     The title of the alert dialog.
     * @param message   The message to display in the alert dialog.
     * @author Shayan Yamanidouzi Sorkhabi
     */
    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
