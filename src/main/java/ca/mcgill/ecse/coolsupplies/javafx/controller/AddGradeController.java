package ca.mcgill.ecse.coolsupplies.javafx.controller;

import ca.mcgill.ecse.coolsupplies.controller.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;


public class AddGradeController {

    @FXML
    private TextField gradeInputField; // TextField for user input of grade

    @FXML
    private Button addGradeButton; // Button to trigger adding the grade

    @FXML
    private void handleAddGrade(ActionEvent event) {
        // Get user input
        String gradeLevel = gradeInputField.getText().trim();

        // Check if input is empty
        if (gradeLevel.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Grade number cannot be empty.");
            return;
        }

        // Call pre-existing addGrade method in CoolSuppliesFeatureSet7Controller
        String result = CoolSuppliesFeatureSet7Controller.addGrade(gradeLevel);

        // Show appropriate alert based on the result
        if (result.contains("error") || result.contains("Error")) {
            showAlert(Alert.AlertType.ERROR, "Add Grade Failed", result);
        } else {
            showAlert(Alert.AlertType.INFORMATION, "Add Grade Success", result);
            // Close the current stage after success (optional)
            addGradeButton.getScene().getWindow().hide();
        }
    }

    // Helper method to display alerts
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
