package ca.mcgill.ecse.coolsupplies.javafx.pages;

import ca.mcgill.ecse.coolsupplies.controller.*;
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

public class UpdateStudentController implements Initializable {

    @FXML
    private ComboBox<String> gradeComboBox;

    @FXML
    private TextField nameTextField;

    @FXML
    private Button updateButton;

    private static TOStudent student;

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

    public static void setExistingName(TOStudent s) {
        student = s;
    }

    private void throwErrorWindow(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
