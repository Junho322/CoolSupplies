package ca.mcgill.ecse.coolsupplies.javafx.pages;

import ca.mcgill.ecse.coolsupplies.controller.CoolSuppliesFeatureSet1Controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;

public class UpdatePasswordController {

    @FXML
    private Button confirmButton;

    @FXML
    private PasswordField newPasswordField;

    @FXML
    private PasswordField oldPasswordField;

    @FXML
    void confirmUpdatePassword(ActionEvent event) {
        String oldPassword = oldPasswordField.getText();
        String newPassword = newPasswordField.getText();

        if (oldPassword.isEmpty() || newPassword.isEmpty()) {
            throwErrorWindow("Please fill in all fields");
            oldPasswordField.clear();
            newPasswordField.clear();
            return;
        }

        if (!CoolSuppliesFeatureSet1Controller.matchesCurrentPassword(oldPassword)) {
            throwErrorWindow("Old password is incorrect");
            oldPasswordField.clear();
            newPasswordField.clear();
            return;
        }

        String status = CoolSuppliesFeatureSet1Controller.updateAdmin(newPassword);

        if (!status.equals("Admin password updated successfully")) {
            throwErrorWindow(status);
            oldPasswordField.clear();
            newPasswordField.clear();
            return;
        }
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText("Admin password updated successfully");
        alert.showAndWait();

        Stage stage = (Stage) confirmButton.getScene().getWindow();
        stage.close();
    }

    private void throwErrorWindow(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
