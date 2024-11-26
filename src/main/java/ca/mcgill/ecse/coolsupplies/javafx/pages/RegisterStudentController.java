package ca.mcgill.ecse.coolsupplies.javafx.pages;

import ca.mcgill.ecse.coolsupplies.controller.CoolSuppliesFeatureSet2Controller;
import ca.mcgill.ecse.coolsupplies.controller.CoolSuppliesFeatureSet7Controller;
import ca.mcgill.ecse.coolsupplies.controller.TOGrade;
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
import java.util.Objects;
import java.util.ResourceBundle;

public class RegisterStudentController implements Initializable {

    @FXML
    private ComboBox<String> gradeComboBox;

    @FXML
    private TextField nameTextField;

    @FXML
    private Button registerButton;

    @FXML
    void registerStudent(ActionEvent event) {
        String name = nameTextField.getText();
        String grade = (String) gradeComboBox.getValue();

        String status = CoolSuppliesFeatureSet2Controller.addStudent(name, grade);

        if (!Objects.equals(status, "Student successfully added.")) {
            throwErrorWindow(status);
            return;
        }

        Stage stage = (Stage) registerButton.getScene().getWindow();
        stage.close();

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setHeaderText("Student successfully added.");
        alert.showAndWait();
    }

    private void throwErrorWindow(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ArrayList<TOGrade> grades = new ArrayList<>(CoolSuppliesFeatureSet7Controller.getGrades());
        ArrayList<String> gradeLevels = new ArrayList<>();
        for (TOGrade grade : grades) {
            gradeLevels.add(grade.getLevel());
        }
        gradeComboBox.setItems(FXCollections.observableArrayList(gradeLevels).sorted());
    }
}
