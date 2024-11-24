package ca.mcgill.ecse.coolsupplies.javafx.pages;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import ca.mcgill.ecse.coolsupplies.controller.CoolSuppliesFeatureSet7Controller;
import ca.mcgill.ecse.coolsupplies.controller.TOGrade;

public class GradePageController {

    @FXML
    private ListView<String> gradeListView;

    @FXML
    private Button addGradeButton;

    private ObservableList<String> gradeList;

    /**
     * Initializes the GradePageController and loads the grade list.
     */
    @FXML
    public void initialize() {
        loadGrades();
    }

    /**
     * Loads grades from the controller and populates the ListView.
     */
    private void loadGrades() {
        List<TOGrade> grades = CoolSuppliesFeatureSet7Controller.getGrades();
        gradeList = FXCollections.observableArrayList(
                grades.stream().map(TOGrade::getLevel).collect(Collectors.toList())
        );
        gradeListView.setItems(gradeList);
    }

    /**
     * Refreshes the grade list by reloading the grades from the controller.
     */
    public void refreshGrades() {
        loadGrades();
    }

    /**
     * Handles the Add Grade button click by opening the Grade.fxml scene.
     *
     * @param event the ActionEvent triggered by the Add Grade button
     */
    @FXML
    private void handleAddGrade(ActionEvent event) {
        try {
            // Load the Grade.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("./Grade.fxml"));
            Parent root = loader.load();

            // Get the GradeController instance
            GradeController gradeController = loader.getController();

            // Pass the current GradePageController instance to GradeController
            gradeController.setGradePageController(this);

            // Open the Grade.fxml scene in a new modal window
            Stage stage = new Stage();
            stage.setTitle("Grade Details");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.showAndWait(); // Wait for the modal window to close
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Error", "Could not open the Add Grade page.");
        }
    }

    /**
     * Displays an alert message to the user.
     *
     * @param type    the AlertType (e.g., INFORMATION, ERROR)
     * @param title   the title of the alert
     * @param content the message content of the alert
     */
    private void showAlert(AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
