package ca.mcgill.ecse.coolsupplies.javafx.pages;

import ca.mcgill.ecse.coolsupplies.controller.CoolSuppliesFeatureSet1Controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.Objects;

public class RegisterParentController {

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

    @FXML
    void registerParent(ActionEvent event) {
        String e = email.getText();
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

        String status = CoolSuppliesFeatureSet1Controller.addParent(e, p, n, ph);

        if (!Objects.equals(status, "Parent added successfully")) {
            throwErrorWindow(status);
            return;
        }

        //close window
        Stage stage = (Stage) registerButton.getScene().getWindow();
        stage.close();
    }

    public void initialize() {
    }

    private void throwErrorWindow(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
