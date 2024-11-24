package ca.mcgill.ecse.coolsupplies.javafx.pages;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import ca.mcgill.ecse.coolsupplies.controller.CoolSuppliesFeatureSet7Controller;
import ca.mcgill.ecse.coolsupplies.controller.TOGrade;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class GradePageController implements Initializable {

    @FXML
    private ListView<String> gradeListView;

    @FXML
    private Button addGradeButton;

    private ObservableList<String> gradeList;

    /**
     * Initializes the GradePageController and loads the grade list.
     */
    @FXML
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //loadGrades();
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
    void addGrade(ActionEvent event) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Grade.fxml"));
            Parent root1 = loader.load();
            Stage stage = new Stage();

            stage.setTitle("Add Grade");
            stage.setScene(new Scene(root1));
            stage.initModality(Modality.APPLICATION_MODAL); // Set the modality to APPLICATION_MODAL
            stage.showAndWait(); // Use showAndWait to block the admin page until the register parent window is closed

       

            initialize(null, null);

        } catch (IOException e) {
            e.printStackTrace();
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
