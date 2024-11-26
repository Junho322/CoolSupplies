package ca.mcgill.ecse.coolsupplies.javafx.controller;

import ca.mcgill.ecse.coolsupplies.application.CoolSuppliesApplication;
import ca.mcgill.ecse.coolsupplies.controller.*;
import ca.mcgill.ecse.coolsupplies.model.BundleItem;
import ca.mcgill.ecse.coolsupplies.model.CoolSupplies;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.ArrayList;
import java.util.List;

public class BundlePageController {

    private final ArrayList<String> discountApplyChoices = new ArrayList<>();

    private CoolSupplies coolSupplies = CoolSuppliesApplication.getCoolSupplies();

    private String selectedBundleName;

    // Fields linked to FXML elements
    @FXML
    private TextField nameField;

    @FXML
    private TextField discountField;

    @FXML
    private ComboBox<String> gradeChoice;

    @FXML
    private TextField editBundle;

    @FXML
    private ComboBox<String> editGrade;

    @FXML
    private TextField editDiscount;

    @FXML
    private Button buttonToEdit;

    @FXML
    private Button saveButton;

    @FXML
    private Button deleteButton;

    @FXML
    private ListView<String> listView;

    private ObservableList<String> bundles = FXCollections.observableArrayList();

    // Initialize method (called automatically when the FXML file is loaded)
    @FXML
    private void initialize() {
        // Initialize the list with any preexisting bundles
        List<TOGradeBundle> allBundles = CoolSuppliesFeatureSet4Controller.getBundles();
        for (TOGradeBundle bundle : allBundles) {
            bundles.add(bundle.getName());
        }
        listView.setItems(bundles);

        // Set all dropdown menu values
        ArrayList<TOGrade> grades = new ArrayList<>();
        ArrayList<String> gradeNames = new ArrayList<>();
        grades.addAll(CoolSuppliesFeatureSet7Controller.getGrades());
        for (TOGrade grade : grades) {
            gradeNames.add(grade.getLevel());
        }
        discountApplyChoices.add("Yes"); discountApplyChoices.add("No");
        gradeChoice.getItems().addAll(gradeNames);
        editGrade.getItems().addAll(gradeNames);
        populateListView();
        // Add a listener for bundle selection
        listView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                handleBundleSelection(newVal);
            }
        });
    }

    @FXML
    private void handleAddButton() {
        String name = nameField.getText();
        String discount = discountField.getText();
        String grade = gradeChoice.getValue();

        if (name.isEmpty() || discount.isEmpty() || grade == null) {
            showAlert("Error", "All fields must be filled to add a bundle.");
            return;
        }

        try {
            int discountValue = Integer.parseInt(discount);

            String result = CoolSuppliesFeatureSet4Controller.addBundle(name, discountValue, grade);
            if (!result.equals("GradeBundle added successfully.")) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText(result);
                alert.showAndWait();
                return;
            }
            bundles.add(name);

            System.out.println("Bundle added: " + name);

        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Invalid discount value. Enter a number.");
            alert.showAndWait();
        } finally {
            nameField.clear();
            discountField.clear();
            populateListView();
        }
    }

    @FXML
    private void handleEditItems() {


    }

    @FXML
    private void handleSaveButton() {
        String newName = editBundle.getText();
        String newDiscount = editDiscount.getText();
        String newGrade = editGrade.getValue();
        if (newName.isEmpty() || newDiscount.isEmpty() || newGrade == null) {
            showAlert("Error", "All fields must be filled to edit a bundle.");
            return;
        }

        try {
            int discountValue = Integer.parseInt(newDiscount);
            String result = "ok";
            if (!result.equals("Successfully updated Bundle")) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText(result);
                alert.showAndWait();
            }

        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Invalid discount value. Enter a number.");
            alert.showAndWait();
        } finally {
            nameField.clear();
            discountField.clear();
            populateListView();
        }
    }

    @FXML
    private void handleDeleteButton(ActionEvent event) {

        // Create a confirmation alert
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Confirm Deletion");
        confirmationAlert.setHeaderText("Delete Bundle");
        confirmationAlert.setContentText("Are you sure you want to delete the bundle?");

        // Wait for the user's response
        confirmationAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                bundles.remove(selectedBundleName);
                CoolSuppliesFeatureSet4Controller.deleteBundle(selectedBundleName);
                showAlert("Success", "Bundle deleted successfully.");
            } else {
                // User canceled the action
                System.out.println("Deletion canceled by the user.");
            }
        });
    }


    // Handle bundle selection from the ListView
    private void handleBundleSelection(String selectedBundle) {
        selectedBundleName = selectedBundle;
        System.out.println("Selected bundle: " + selectedBundle);

        // Example: Display the bundle details (customize as needed)
        // This is where you can load more details for the selected bundle.
    }

    private void populateListView() {listView.setItems(bundles);}

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
