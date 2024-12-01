package ca.mcgill.ecse.coolsupplies.javafx.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.Node;
/**
 * Controller for the "Show Students Page" in a JavaFX application.
 * This class handles user interactions and UI updates for managing student information.
 *
 * <p>Features include:</p>
 * <ul>
 * <li>Adding new students</li>
 * <li>Editing existing students</li>
 * <li>Removing students</li>
 * </ul>
 *
 * @author Hamza Khalfi
 */
public class ShowStudentsPageController {
    @FXML private VBox editPanel;
    @FXML private VBox createPanel;
    @FXML private VBox addStudentCard;
    @FXML private TextField nameField;
    @FXML private ComboBox<String> createGradeComboBox;
    @FXML private ComboBox<String> editGradeComboBox;
    @FXML private ComboBox<String> filterGradeComboBox;
    @FXML private Label editingStudentName;
    @FXML private Button saveNewButton;
    @FXML private Button cancelButton;
    @FXML private Button saveEditButton;
    @FXML private Button removeButton;
    @FXML private FlowPane studentCardsContainer;

    /**
     * Initializes the controller, setting up ComboBox values, default visibility states,
     * and event handlers for UI components.
     *
     * @author Hamza Khalfi
     */
    @FXML
    public void initialize() {
        // Initialize ComboBox values
        String[] grades = {"5", "6", "7", "8", "9", "10", "11", "12"};
        createGradeComboBox.getItems().addAll(grades);
        editGradeComboBox.getItems().addAll(grades);
        filterGradeComboBox.getItems().addAll(grades);

        // Set default values
        createGradeComboBox.setValue("5");
        editGradeComboBox.setValue("5");
        filterGradeComboBox.setValue("5");

        // Initialize visibility states
        editPanel.setVisible(true);
        createPanel.setVisible(false);

        // Set up event handlers
        addStudentCard.setOnMouseClicked(this::handleAddStudentClick);
        cancelButton.setOnAction(e -> switchToEditMode());
        saveNewButton.setOnAction(e -> saveNewStudent());
        saveEditButton.setOnAction(e -> updateExistingStudent());
        removeButton.setOnAction(e -> removeExistingStudent());
    }

    /**
     * Handles the "Add Student" card click event to switch to create mode.
     *
     * @param event the MouseEvent triggered by clicking the "Add Student" card
     * @author Hamza Khalfi
     */
    @FXML
    private void handleAddStudentClick(MouseEvent event) {
        switchToCreateMode();
    }

    /**
     * Switches the UI to create mode, enabling the panel for adding a new student.
     *
     * @author Hamza Khalfi
     */
    @FXML
    private void switchToCreateMode() {
        editPanel.setVisible(false);
        createPanel.setVisible(true);
        nameField.clear();
        createGradeComboBox.setValue("5");
    }

    /**
     * Switches the UI to edit mode, enabling the panel for editing existing students.
     *
     * @author Hamza Khalfi
     */
    @FXML
    private void switchToEditMode() {
        createPanel.setVisible(false);
        editPanel.setVisible(true);
    }

    /**
     * Saves a new student by creating a new card in the UI.
     * Validates input and shows an error alert if the input is invalid.
     *
     * @author Hamza Khalfi
     */
    @FXML
    private void saveNewStudent() {
        String name = nameField.getText();
        String grade = createGradeComboBox.getValue();

        if (name != null && !name.trim().isEmpty()) {
            // Create new student card
            VBox studentCard = createStudentCard(name, grade);

            // Add the new card before the "Add Student" card
            int lastIndex = studentCardsContainer.getChildren().size() - 1;
            studentCardsContainer.getChildren().add(lastIndex, studentCard);

            // Switch back to edit mode
            switchToEditMode();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Invalid Input");
            alert.setContentText("Please enter a student name.");
            alert.showAndWait();
        }
    }

    /**
     * Updates an existing student's information in the UI.
     *
     * @return void (Updates the UI directly without returning a value)
     * @author Hamza Khalfi
     */
    @FXML
    private void updateExistingStudent() {
        String currentName = editingStudentName.getText();
        String newGrade = editGradeComboBox.getValue();

        // Find and update the corresponding student card
        for (Node node : studentCardsContainer.getChildren()) {
            if (node instanceof VBox && !node.equals(addStudentCard)) {
                VBox card = (VBox) node;
                Label nameLabel = (Label) card.getChildren().get(0);
                if (nameLabel.getText().equals(currentName)) {
                    Label gradeLabel = (Label) card.getChildren().get(1);
                    gradeLabel.setText("Grade " + newGrade);
                    break;
                }
            }
        }
    }

    /**
     * Removes an existing student from the UI based on the selected student's name.
     * void (Removes the student card directly from the UI without returning a value)
     *
     * @author Hamza Khalfi
     */
    @FXML
    private void removeExistingStudent() {
        String currentName = editingStudentName.getText();

        // Remove the corresponding student card
        studentCardsContainer.getChildren().removeIf(node -> {
            if (node instanceof VBox && !node.equals(addStudentCard)) {
                VBox card = (VBox) node;
                Label nameLabel = (Label) card.getChildren().get(0);
                return nameLabel.getText().equals(currentName);
            }
            return false;
        });

        // Reset the editing panel
        editingStudentName.setText("No Student Selected");
        editGradeComboBox.setValue("5");
    }

    /**
     * Creates a new student card with the given name and grade.
     *
     * @param name the student's name
     * @param grade the student's grade
     * @return a VBox representing the new student card
     *
     * @author Hamza Khalfi
     */
    private VBox createStudentCard(String name, String grade) {
        VBox card = new VBox(10);
        card.getStyleClass().add("student-card");
        card.setStyle("-fx-background-color: #f5f5f5; -fx-padding: 15; -fx-background-radius: 10; " +
                "-fx-border-radius: 10; -fx-border-color: #ddd; -fx-border-width: 1; " +
                "-fx-min-width: 200; -fx-min-height: 150; -fx-alignment: center;");

        Label nameLabel = new Label(name);
        nameLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        Label gradeLabel = new Label("Grade " + grade);
        gradeLabel.setStyle("-fx-font-size: 14px;");

        card.getChildren().addAll(nameLabel, gradeLabel);

        // Add click handler to edit this student
        card.setOnMouseClicked(e -> {
            editingStudentName.setText(name);
            editGradeComboBox.setValue(grade);
            switchToEditMode();
        });

        return card;
    }
}
