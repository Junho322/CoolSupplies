package ca.mcgill.ecse.coolsupplies.javafx.controller;

import ca.mcgill.ecse.coolsupplies.controller.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;


public class UpdateGradeController {

    @FXML
    private Label currentGradeLabel; // Label to display the current grade

    @FXML
    private TextField gradeInputField; // TextField for user input of grade

    @FXML
    private Button updateGradeButton; // Button to trigger adding the grade

    @FXML
    private Button deleteGradeButton;

    private TOGrade selectedGrade;

    public void setSelectedGrade(TOGrade grade) {
      this.selectedGrade = grade;
  
      // Populate the current grade details in the UI
      currentGradeLabel.setText("Current Grade: " + grade.getLevel());
    }

    @FXML
    private void handleUpdateGrade(ActionEvent event) {
      String newGradeLevel = gradeInputField.getText().trim();
  
      if (newGradeLevel.isEmpty()) {
          showAlert(Alert.AlertType.ERROR, "Invalid Input", "Grade level cannot be empty.");
          return;
      }
  
      String result = CoolSuppliesFeatureSet7Controller.updateGrade(selectedGrade.getLevel(), newGradeLevel);
  
      if (result.contains("error") || result.contains("Error")) {
          showAlert(Alert.AlertType.ERROR, "Update Grade Failed", result);
      } else {
          showAlert(Alert.AlertType.INFORMATION, "Update Grade Success", result);
          updateGradeButton.getScene().getWindow().hide();
      }
  }

    @FXML
    private void handleDeleteGrade(ActionEvent event) {
      String result = CoolSuppliesFeatureSet7Controller.deleteGrade(selectedGrade.getLevel());
  
      if (result.contains("error") || result.contains("Error")) {
          showAlert(Alert.AlertType.ERROR, "Delete Grade Failed", result);
      } else {
          showAlert(Alert.AlertType.INFORMATION, "Delete Grade Success", result);
          deleteGradeButton.getScene().getWindow().hide();
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
