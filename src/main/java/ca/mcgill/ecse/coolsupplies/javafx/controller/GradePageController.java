package ca.mcgill.ecse.coolsupplies.javafx.controller;

import ca.mcgill.ecse.coolsupplies.controller.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.util.List;
import java.util.stream.Collectors;

public class GradePageController {

    @FXML
    private ListView<String> gradeListView;

    @FXML
    private TextField gradeTextField;

    @FXML
    private TextField updateGradeTextField;

    @FXML
    private Button addGradeButton;

    @FXML
    private Button updateGradeButton;

    @FXML
    private Button deleteGradeButton;

    private ObservableList<String> gradeList;

    @FXML
    public void initialize() {
        loadGrades();
    }

    private void loadGrades() {
        List<TOGrade> grades = CoolSuppliesFeatureSet7Controller.getGrades();
        gradeList = FXCollections.observableArrayList(
                grades.stream().map(TOGrade::getLevel).collect(Collectors.toList())
        );
        gradeListView.setItems(gradeList);
    }

    @FXML
    private void handleAddGrade(ActionEvent event) {
        String gradeLevel = gradeTextField.getText().trim();
        if (gradeLevel.isEmpty()) {
            showAlert(AlertType.ERROR, "Add Grade", "The grade level must not be empty.");
            return;
        }

        String result = CoolSuppliesFeatureSet7Controller.addGrade(gradeLevel);
        showAlert(AlertType.INFORMATION, "Add Grade", result);

        if (result.contains("successfully")) {
            loadGrades();
            gradeTextField.clear();
        }
    }

    @FXML
    private void handleUpdateGrade(ActionEvent event) {
        String selectedGrade = gradeListView.getSelectionModel().getSelectedItem();
        String newGradeLevel = updateGradeTextField.getText().trim();

        if (selectedGrade == null) {
            showAlert(AlertType.ERROR, "Update Grade", "No grade selected to update.");
            return;
        }

        if (newGradeLevel.isEmpty()) {
            showAlert(AlertType.ERROR, "Update Grade", "The new grade level must not be empty.");
            return;
        }

        String result = CoolSuppliesFeatureSet7Controller.updateGrade(selectedGrade, newGradeLevel);
        showAlert(AlertType.INFORMATION, "Update Grade", result);

        if (result.contains("successfully")) {
            loadGrades();
            updateGradeTextField.clear();
        }
    }

    @FXML
    private void handleDeleteGrade(ActionEvent event) {
        String selectedGrade = gradeListView.getSelectionModel().getSelectedItem();

        if (selectedGrade == null) {
            showAlert(AlertType.ERROR, "Delete Grade", "No grade selected to delete.");
            return;
        }

        String result = CoolSuppliesFeatureSet7Controller.deleteGrade(selectedGrade);
        showAlert(AlertType.INFORMATION, "Delete Grade", result);

        if (result.contains("successfully")) {
            loadGrades();
        }
    }

    private void showAlert(AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
