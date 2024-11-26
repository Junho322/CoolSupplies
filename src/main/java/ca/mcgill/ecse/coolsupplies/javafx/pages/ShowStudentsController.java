package ca.mcgill.ecse.coolsupplies.javafx.pages;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.nio.file.Paths;
import java.util.List;
import javafx.fxml.FXML;

import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import ca.mcgill.ecse.coolsupplies.controller.TOGrade;
import ca.mcgill.ecse.coolsupplies.controller.TOStudent;
import ca.mcgill.ecse.coolsupplies.controller.CoolSuppliesFeatureSet2Controller;
import ca.mcgill.ecse.coolsupplies.controller.CoolSuppliesFeatureSet7Controller;



public class ShowStudentsController {


    @FXML
    private ComboBox<String> editGradeComboBox;

    @FXML
    private ComboBox<String> createGradeComboBox;



    @FXML
    private Label editingStudentName;
    @FXML
    private TextField editNameField;

    @FXML
    private Button saveEditButton;

    @FXML
    private Button removeButton;

    @FXML
    private TextField nameField;

    @FXML
    private Button saveNewButton;

    @FXML
    private Button cancelButton;

    @FXML
    private ComboBox<String> filterGradeComboBox;

    @FXML
    private FlowPane studentCardsContainer;

    @FXML
    private Button addStudentButton;

    @FXML
    private VBox editPanel;

    @FXML
    private VBox createPanel;
    @FXML
    private VBox instructionsPanel;


    @FXML
    public void initialize() {
        // Show Instructions Panel by default

        instructionsPanel.setVisible(true);
        editPanel.setVisible(false);
        createPanel.setVisible(false);

        // Event Handlers
        addStudentButton.setOnAction(event -> handleAddStudentButtonAction());
        cancelButton.setOnAction(event -> handleCancelButtonAction());
        saveNewButton.setOnAction(event -> handleCreateStudent());
        populateGrades();
        filterGradeComboBox.setOnAction(event -> filterStudentsByGrade());
    }




    @FXML
    private void handleCancelButtonAction() {
        System.out.println("Cancel Button Clicked");

        // Hide createPanel and show editPanel
        createPanel.setVisible(false);
        editPanel.setVisible(true);
    }

    @FXML
    private void handleAddStudentButtonAction() {
        System.out.println("Add Student Button Clicked");

        // Hide editPanel and show createPanel
        editPanel.setVisible(false);
        createPanel.setVisible(true);
    }

    private void populateGrades() {
        // Retrieve the list of grades using the controller method
        List<TOGrade> grades = CoolSuppliesFeatureSet7Controller.getGrades();

        // Create an ObservableList to hold the grade levels
        ObservableList<String> gradeList = FXCollections.observableArrayList();

        // Loop through the grades and add the grade levels to the list
        for (TOGrade grade : grades) {
            gradeList.add(grade.getLevel()); // Assuming TOGrade has a getLevel() method
        }

        // Set the items for both ComboBoxes
        editGradeComboBox.setItems(gradeList);
        createGradeComboBox.setItems(gradeList);
        filterGradeComboBox.setItems(gradeList);
    }

    private void filterStudentsByGrade() {
        // Clear current student cards but keep the "Add Student" button
        studentCardsContainer.getChildren().clear();
        studentCardsContainer.getChildren().add(addStudentButton);

        // Get the selected grade
        String selectedGrade = filterGradeComboBox.getValue();
        if (selectedGrade == null) return;

        // Re-fetch students filtered by grade
        List<TOStudent> students = CoolSuppliesFeatureSet2Controller.getStudents();
        for (TOStudent student : students) {
            if (student.getGradeLevel().equals(selectedGrade)) {
                studentCardsContainer.getChildren().add(createStudentCard(student));
            }
        }

        // Ensure panels visibility reset
        instructionsPanel.setVisible(true);
        editPanel.setVisible(false);
        createPanel.setVisible(false);
    }


    private Button createStudentCard(TOStudent student) {
        // Create the student card as a Button
        Button card = new Button();
        card.setStyle("-fx-background-color: #f0f8ff; -fx-padding: 10; -fx-border-color: #ccc; -fx-border-width: 1; -fx-border-radius: 10; -fx-background-radius: 10;");
        card.setPrefSize(150, 200); // Same size as the "Add Student" button

        // Disable focus ring for aesthetics
        card.setFocusTraversable(false);

        // Add the avatar image
        ImageView avatar;
        try {
            // Attempt to load from resources
            String imagePath = getClass().getResource("/images/avatar.png").toExternalForm();
            avatar = new ImageView(new Image(imagePath));
        } catch (Exception e) {
            // Fallback if resource is not found
            String fallbackPath = Paths.get("src/main/resources/images/avatar.png").toUri().toString();
            avatar = new ImageView(new Image(fallbackPath));
        }
        avatar.setFitWidth(120);
        avatar.setFitHeight(120);
        avatar.setPreserveRatio(true);


        Label nameLabel = new Label(student.getName());
        nameLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-alignment: center;");

        // Add the student's grade
        Label gradeLabel = new Label("Grade: " + student.getGradeLevel());
        gradeLabel.setStyle("-fx-font-size: 12px; -fx-text-alignment: center; -fx-text-fill: #666;");

        // Create a VBox for the content inside the button
        VBox content = new VBox(10, avatar, nameLabel, gradeLabel);
        content.setAlignment(javafx.geometry.Pos.CENTER);

        // Add the content to the button
        card.setGraphic(content);

        // Set the action to edit the student on click
        card.setOnAction(event -> editStudent(student));

        return card;
    }


    private void editStudent(TOStudent student) {
        instructionsPanel.setVisible(false);
        createPanel.setVisible(false);
        editPanel.setVisible(true);

        editingStudentName.setText("EDITING: " + student.getName());
        editingStudentName.setStyle("-fx-font-weight: bold; -fx-font-size: 16px; -fx-text-fill: #333;");
        editNameField.setText(student.getName());

        editGradeComboBox.setValue(student.getGradeLevel());


        saveEditButton.setOnAction(event ->{
                String newName = editNameField.getText();
                String newGrade = editGradeComboBox.getValue();
                showUpdateConfirmationDialog(student,newName,newGrade);
        });


        removeButton.setOnAction(event -> showDeleteConfirmationDialog(student));
    }

    private void showErrorPopup(String message) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("An Error Occurred");
        alert.setContentText(message);

        alert.showAndWait();
    }


    private void showUpdateConfirmationDialog(TOStudent student,String newName, String newGrade) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Update");
        alert.setHeaderText("Are you sure you want to update this student?");

        ButtonType yesButton = new ButtonType("Yes");
        ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(yesButton, noButton);

        alert.showAndWait().ifPresent(response -> {
            if (response == yesButton) {

                String result = CoolSuppliesFeatureSet2Controller.updateStudent(student.getName(), newName, newGrade);

                if (result.equals("Student successfully updated.")) {
                    showSuccessPopup(result);
                    filterStudentsByGrade();
                    editPanel.setVisible(false);
                    instructionsPanel.setVisible(true);

                } else {

                    showErrorPopup(result);

                }
            }

        });

    }
    private void showDeleteConfirmationDialog(TOStudent student) {

        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Deletion");
        alert.setHeaderText("Are you sure you want to delete this student?");
        alert.setContentText("This action cannot be undone.");


        ButtonType yesButton = new ButtonType("Yes");
        ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(yesButton, noButton);


        alert.showAndWait().ifPresent(response -> {
            if (response == yesButton) {

                String result = CoolSuppliesFeatureSet2Controller.deleteStudent(student.getName());

                if (result.equals("Success")) {

                    filterStudentsByGrade();
                } else {

                    showErrorPopup(result);
                }
            }

        });
    }

    private void handleCreateStudent() {
        String name = nameField.getText(); // Get the name input
        String grade = createGradeComboBox.getValue(); // Get the selected grade

        // Validate the inputs
        if (name == null || name.trim().isEmpty()) {
            showErrorPopup("The name field cannot be empty.");
            return;
        }
        if (grade == null || grade.trim().isEmpty()) {
            showErrorPopup("Please select a grade.");
            return;
        }

        // Attempt to add the student using the controller
        String result = CoolSuppliesFeatureSet2Controller.addStudent(name.trim(), grade);

        if (result != null) {
            // Success: Refresh the student list
            filterStudentsByGrade();
            showSuccessPopup(result);


            // Clear the inputs
            nameField.clear();
            createGradeComboBox.setValue(null);
        } else {
            // Error: Show the error message
            showErrorPopup("Add failed");
        }
    }




    private void showSuccessPopup(String message) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }









}
