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

/**
 * Controller class that handles operations related to the Register Parent page in the system.
 * Methods implemented include registering a parent in the system.
 *
 * @author Jack McDonald
 */
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

    /**
     * Handles the click event on the Register button
     *
     * @param event The ActionEvent that triggered the event
     * @author Jack McDonald
     */
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

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText("Parent registered successfully");
        alert.showAndWait();
    }

    /**
     * Initializes the RegisterParent page
     *
     * @author Jack McDonald
     */
    public void initialize() {
    }

    /**
     * Displays an error window with the given message
     *
     * @param message The message to display in the error window
     * @author Jack McDonald
     */
    private void throwErrorWindow(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
