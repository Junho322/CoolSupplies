package ca.mcgill.ecse.coolsupplies.javafx.pages;

import ca.mcgill.ecse.coolsupplies.controller.CoolSuppliesFeatureSet1Controller;
import ca.mcgill.ecse.coolsupplies.controller.TOParent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.Objects;

public class UpdateParentController {

    @FXML
    private TextField email;

    @FXML
    private TextField name;

    @FXML
    private PasswordField password;

    @FXML
    private TextField phone;

    @FXML
    private Button registerButton;

    private static TOParent parent;

    public static void setExistingEmail(TOParent p) {
        parent = p;
    }

    @FXML
    void initialize() {
        email.setText(parent.getEmail());
        name.setText(parent.getName());
        phone.setText(String.valueOf(parent.getPhoneNumber()));
    }

    @FXML
    void updateParent(ActionEvent event) {
        String n = name.getText();
        String p = password.getText();

        int ph = 0;
        try {
            ph = Integer.parseInt(phone.getText().strip());
        } catch (NumberFormatException ex) {
            throwErrorWindow("Phone number must be a number");
        }

        if (phone.getText().contains("[a-zA-Z]+")) {
            throwErrorWindow("Phone number must be a number");
            return;
        }

        String status = CoolSuppliesFeatureSet1Controller.updateParent(parent.getEmail(), p, n, ph);

        if (!Objects.equals(status, "Parent updated successfully")) {
            throwErrorWindow(status);
            return;
        }

        //close window
        Stage stage = (Stage) registerButton.getScene().getWindow();
        stage.close();

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
    }

    private void throwErrorWindow(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Error");
        alert.setContentText(message);
        alert.showAndWait();
    }

}
