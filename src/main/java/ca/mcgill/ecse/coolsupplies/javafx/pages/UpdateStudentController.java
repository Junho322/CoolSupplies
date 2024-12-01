package ca.mcgill.ecse.coolsupplies.javafx.pages;

import ca.mcgill.ecse.coolsupplies.controller.CoolSuppliesFeatureSet2Controller;
import ca.mcgill.ecse.coolsupplies.controller.CoolSuppliesFeatureSet7Controller;
import ca.mcgill.ecse.coolsupplies.controller.TOGrade;
import ca.mcgill.ecse.coolsupplies.controller.TOStudent;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Controller class that handles operations related to the Update Student page in the system.
 *
 * @author Jack McDonald
 */
public class UpdateStudentController implements Initializable {

    private static TOStudent student;
    @FXML
    private ComboBox<String> gradeComboBox;
    @FXML
    private TextField nameTextField;
    @FXML
    private Button updateButton;

    public static void setExistingName(TOStudent s) {
        student = s;
    }

    /**
     * Handles the click event on the Update button
     *
     * @param event The ActionEvent that triggered the event
     * @author Jack McDonald
     */
    @FXML
    void updateStudent(ActionEvent event) {
        String name = nameTextField.getText();
        String grade = gradeComboBox.getValue();

        String status = CoolSuppliesFeatureSet2Controller.updateStudent(student.getName(), name, grade);

        if (!status.equals("Student successfully updated.")) {
            throwErrorWindow(status);
            return;
        }

        Stage stage = (Stage) updateButton.getScene().getWindow();
        stage.close();

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setHeaderText("Student successfully updated.");
        alert.showAndWait();
    }

    /**
     * Initializes the Update Student page
     *
     * @param location  The location used to resolve relative paths for the root object, or
     *                  {@code null} if the location is not known.
     * @param resources The resources used to localize the root object, or {@code null} if
     *                  the root object was not localized.
     * @author Jack McDonald
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ArrayList<TOGrade> grades = new ArrayList<>(CoolSuppliesFeatureSet7Controller.getGrades());
        ArrayList<String> gradeLevels = new ArrayList<>();
        for (TOGrade grade : grades) {
            gradeLevels.add(grade.getLevel());
        }
        gradeComboBox.setItems(FXCollections.observableArrayList(gradeLevels).sorted());

        nameTextField.setText(student.getName());
    }

    /**
     * Throws an error window with the given message
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
