package ca.mcgill.ecse.coolsupplies.javafx.controller;

import ca.mcgill.ecse.coolsupplies.controller.*;
import ca.mcgill.ecse.coolsupplies.javafx.pages.AdminPageController;
import ca.mcgill.ecse.coolsupplies.model.Student;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
    import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Priority;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


/**
 * Controller for managing parent information and student relationships in the GUI.
 *
 * Features Addressed:
 * - Add student to parent.
 * - Remove student from parent.
 *
 * @author David Zhou
 */
public class ParentPageController {

    @FXML
    private Label nameLabel;

    @FXML
    private Label emailLabel;

    @FXML
    private Label phoneLabel;

    @FXML
    private ComboBox<String> searchComboBox;

    private ObservableList<String> allStudentNames;
    private FilteredList<String> filteredStudentNames;

    @FXML
    private HBox studentCardContainer;

    private static final String[] CARD_COLORS = {
            "#FF4081", "#8E24AA", "#1E88E5", "#03A9F4"
    };

    /**
   * Initializes the Parent Page GUI components.
   */
    @FXML
    public void initialize() {
        // Initialize labels with empty values
        nameLabel.setText("Name: ");
        emailLabel.setText("Email: ");
        phoneLabel.setText("Phone: ");

        // Initialize the ComboBox with autocomplete
        setupAutoComplete();
    }

  /**
   * Sets the displayed parent information.
   *
   * @param parent The parent to be displayed.
   */
    public void setParentInfo(TOParent parent) {
        nameLabel.setText("Name: " + parent.getName());
        emailLabel.setText("Email: " + parent.getEmail());
        phoneLabel.setText("Phone: " + formatPhoneNumber(parent.getPhoneNumber()));
    }

    /**
   * Configures the autocomplete functionality for the student search ComboBox.
   */
    private void setupAutoComplete() {
        // Get all students and extract their names
        List<TOStudent> allStudents = CoolSuppliesFeatureSet2Controller.getStudents();
        allStudentNames = FXCollections.observableArrayList(
                allStudents.stream()
                        .map(TOStudent::getName)
                        .collect(Collectors.toList())
        );

        // Create a filtered list that will contain the suggestions
        filteredStudentNames = new FilteredList<>(allStudentNames, p -> true);

        // Add a listener to the ComboBox editor
        searchComboBox.getEditor().textProperty().addListener((obs, oldValue, newValue) -> {
            final TextField editor = searchComboBox.getEditor();
            final String selected = searchComboBox.getSelectionModel().getSelectedItem();

            // If nothing has been selected yet
            if (selected == null || !selected.equals(editor.getText())) {
                filteredStudentNames.setPredicate(item -> {
                    // If no search text, show all options
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }

                    // Compare search text with item text (case-insensitive)
                    String lowerCaseFilter = newValue.toLowerCase();
                    return item.toLowerCase().contains(lowerCaseFilter);
                });

                // Show the popup if editor has focus or if there are matches
                if (!filteredStudentNames.isEmpty()) {
                    searchComboBox.show();
                }
            }
        });

        // Bind the filtered list to the ComboBox items
        searchComboBox.setItems(filteredStudentNames);
    }

  /**
   * Handles the action of changing the parent and navigating to the Admin Page.
   *
   * Loads the AdminPage.fxml and switches the current scene to the Admin Page.
   * Maximizes the window and calls a method in AdminPageController to simulate sorting parents.
   *
   * @param event the ActionEvent triggered by the user interaction.
   * @feature CoolSupplies Feature Set 1 - Parent Management
   */
    @FXML
    private void handleChangeParent(ActionEvent event) {
        try {
            // Load AdminPage.fxml using FXMLLoader
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ca/mcgill/ecse/coolsupplies/javafx/pages/AdminPage.fxml"));
            javafx.scene.Parent adminPageRoot = loader.load();

            // Get the AdminPageController instance
            AdminPageController adminPageController = loader.getController();

            // Get the current stage
            Stage stage = (Stage) nameLabel.getScene().getWindow();

            // Set the new scene
            stage.setScene(new Scene(adminPageRoot));

            // Optionally, maximize the stage
            stage.setMaximized(true);

            // Call the method from AdminPageController (simulate case NAME_DESCENDING)
            adminPageController.toggleParentSort(null); // Pass `null` if `event` is not needed.

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

  /**
   * Adds a student to the currently selected parent.
   *
   * @param event The action event triggered by the GUI.
   */
    @FXML
    private void handleConfirmSearch(ActionEvent event) {
        // Extract the displayed parent's email
        String parentEmail = emailLabel.getText().replace("Email: ", "").trim();

        // Validate the parent email
        if (parentEmail.isEmpty()) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Add Student");
            alert.setHeaderText(null);
            alert.setContentText("No parent is selected.");
            alert.showAndWait();
            return;
        }

        // Get the selected student name
        String studentName = searchComboBox.getValue();

        // Validate the student name
        if (studentName == null || studentName.trim().isEmpty()) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Add Student");
            alert.setHeaderText(null);
            alert.setContentText("Please select a student.");
            alert.showAndWait();
            return;
        }

        // Call the controller method to add the student to the parent
        String result = CoolSuppliesFeatureSet6Controller.addStudentToParent(studentName, parentEmail);

        // Show a success or error message based on the result
        Alert alert;
        if (result.equals("Student added to parent.")) {
            // Refresh the student cards
            List<TOStudent> students = CoolSuppliesFeatureSet6Controller.getStudentsOfParent(parentEmail);
            populateStudentCards(students);
        } else {
            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText(result);
            alert.showAndWait();
        }
    }


  /**
   * Formats the given phone number into a user-friendly string.
   *
   * @param phoneNumber The phone number to format.
   * @return The formatted phone number.
   */
  private String formatPhoneNumber(int phoneNumber) {
    String phoneStr = String.valueOf(phoneNumber);
    // Assuming 7-digit phone number
    return "+1 (123) " + phoneStr.substring(0, 3) + "-" + phoneStr.substring(3);
  }

  private static final String[] COLORS = {"#FF4081", "#8E24AA", "#1E88E5", "#03A9F4"};

  /**
   * Populates the student cards in the UI with the given list of students.
   *
   * @param students The list of students to display.
   */
  public void populateStudentCards(List<TOStudent> students) {
    studentCardContainer.getChildren().clear(); // Clear existing cards

    // Set a fixed spacing for the cards
    studentCardContainer.setSpacing(20);

    int colorIndex = 0;

    for (TOStudent student : students) {
      // Create a card with fixed dimensions
      VBox card = new VBox(20); // Increased spacing between elements in the card
      card.setPrefWidth(300.0);
      card.setPrefHeight(300.0);
      card.setStyle("-fx-background-color: " + COLORS[colorIndex] + "; -fx-background-radius: 10; -fx-padding: 16;");
      colorIndex = (colorIndex + 1) % COLORS.length;


      // Header with Remove Button
      HBox header = new HBox();
      header.setAlignment(Pos.TOP_RIGHT);
      Button removeButton = new Button("X");
      removeButton.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-font-size: 20px;");

      removeButton.setOnAction(e -> {
        handleRemoveStudent(student.getName());
        populateStudentCards(CoolSuppliesFeatureSet6Controller.getStudentsOfParent(
                emailLabel.getText().replace("Email: ", "").trim()
        )); // Refresh cards
      });

      header.getChildren().add(removeButton);

      // Student Details
      Label nameLabel = new Label(student.getName());
      nameLabel.setStyle("-fx-font-size: 36px; -fx-font-weight: bold; -fx-text-fill: white; -fx-padding: 5;");
      Separator separator = new Separator();
      separator.setStyle("-fx-background-color: white;");
      Label gradeLabel = new Label("Grade: " + student.getGradeLevel());
      gradeLabel.setStyle("-fx-font-size: 28px; -fx-text-fill: white; -fx-padding: 10;");

      VBox detailsContainer = new VBox(10, nameLabel, separator, gradeLabel);
      detailsContainer.setAlignment(Pos.CENTER);

      // Add elements to card
      card.getChildren().addAll(header, detailsContainer);

      // Add the card to the container
      studentCardContainer.getChildren().add(card);
    }
  }


  /**
   * Removes a student from the currently selected parent.
   *
   * @param studentName The name of the student to be removed.
   */
  private void handleRemoveStudent(String studentName) {
    String parentEmail = emailLabel.getText().replace("Email: ", "").trim();

    if (parentEmail.isEmpty()) {
      System.out.println("Parent not selected.");
      return;
    }

    // Call deleteStudentFromParent
    String result = CoolSuppliesFeatureSet6Controller.deleteStudentFromParent(studentName, parentEmail);

    if (result.equals("Student removed from parent.")) {
      System.out.println("Success: " + result);

      // Refresh the student cards
      List<TOStudent> students = CoolSuppliesFeatureSet6Controller.getStudentsOfParent(parentEmail);
      populateStudentCards(students);
    } else {
      System.out.println("Error: " + result);
    }
  }

  /**
   * Switches the current scene to the Admin Page.
   *
   * Loads AdminPage.fxml, sets it as the new scene, and maximizes the window.
   * Initializes the AdminPageController after loading the scene.
   *
   * @param event the ActionEvent triggered by the user interaction.
   * @feature CoolSupplies Feature Set 1 - Parent Management
   */
  @FXML
  void doSwitchToAdminPage(ActionEvent event) {
      try {
          FXMLLoader loader = new FXMLLoader(getClass().getResource("/ca/mcgill/ecse/coolsupplies/javafx/pages/AdminPage.fxml"));
          Parent root = loader.load();
          Stage stage = (Stage) nameLabel.getScene().getWindow();
          Scene scene = new Scene(root);
          stage.setScene(scene);
          stage.setMaximized(true);
          stage.setResizable(true);
          stage.setTitle("CoolSupplies");
          stage.setWidth(stage.getMaxWidth());
          stage.setHeight(stage.getMaxHeight());
          stage.show();
          AdminPageController controller = loader.getController();
          controller.initialize(null, null);
      } catch (IOException e) {
          e.printStackTrace();
      }
  }

  /**
   * Switches the current scene to the Order Page.
   *
   * Loads OrderPage.fxml, sets it as the new scene, and maximizes the window.
   *
   * @param event the ActionEvent triggered by the user interaction.
   * @feature CoolSupplies Feature Set 3 - Order Management
   */
  @FXML
  void doSwitchToOrderPage(ActionEvent event) {
      try {
          FXMLLoader loader = new FXMLLoader(getClass().getResource("/ca/mcgill/ecse/coolsupplies/javafx/pages/Order/OrderPage.fxml"));
          Parent root = loader.load();
          Stage stage = (Stage) nameLabel.getScene().getWindow();
          Scene scene = new Scene(root);
          stage.setScene(scene);
          stage.setMaximized(true);
          stage.setResizable(true);
          stage.setTitle("CoolSupplies");
          stage.setWidth(stage.getMaxWidth());
          stage.setHeight(stage.getMaxHeight());
          stage.show();
      } catch (IOException e) {
          e.printStackTrace();
      }
  }

  /**
   * Switches the current scene to the Inventory Page.
   *
   * Loads InventoryPage.fxml, sets it as the new scene, and maximizes the window.
   *
   * @param event the ActionEvent triggered by the user interaction.
   * @feature CoolSupplies Feature Set 4 - Inventory Management
   */
  @FXML
  void doSwitchToInventoryPage(ActionEvent event) {
      try {
          FXMLLoader loader = new FXMLLoader(getClass().getResource("/ca/mcgill/ecse/coolsupplies/javafx/pages/InventoryPage.fxml"));
          Parent root = loader.load();
          Stage stage = (Stage) nameLabel.getScene().getWindow();
          Scene scene = new Scene(root);
          stage.setScene(scene);
          stage.setMaximized(true);
          stage.setResizable(true);
          stage.setTitle("CoolSupplies");
          stage.setWidth(stage.getMaxWidth());
          stage.setHeight(stage.getMaxHeight());
          stage.show();
      } catch (IOException e) {
          e.printStackTrace();
      }
  }

  @FXML
  void doSwitchToBundlePage(ActionEvent event) {
      try {
          FXMLLoader loader = new FXMLLoader(getClass().getResource("/ca/mcgill/ecse/coolsupplies/javafx/pages/BundlePage.fxml"));
          Parent root = loader.load();
          Stage stage = (Stage) nameLabel.getScene().getWindow();
          Scene scene = new Scene(root);
          stage.setScene(scene);
          stage.setMaximized(true);
          stage.setResizable(true);
          stage.setTitle("CoolSupplies");
          stage.setWidth(stage.getMaxWidth());
          stage.setHeight(stage.getMaxHeight());
          stage.show();
      } catch (IOException e) {
          e.printStackTrace();
      }
  }

  /**
   * Logs out the current user and switches to the Login Page.
   *
   * Loads LoginPage.fxml, sets it as the new scene, and resizes the window
   * to default dimensions.
   *
   * @param event the ActionEvent triggered by the user interaction.
   * @feature CoolSupplies Feature Set 0 - User Management
   */
    @FXML
    void doLogout(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ca/mcgill/ecse/coolsupplies/javafx/LoginPage.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) nameLabel.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setMaximized(false);
            stage.setResizable(false);
            stage.setTitle("CoolSupplies");
            stage.setX(100);
            stage.setY(100);
            stage.setHeight(600);
            stage.setWidth(800);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

  /**
   * Switches the current scene to the Bundle Page.
   *
   * Loads BundlePage.fxml, sets it as the new scene, and maximizes the window.
   *
   * @param event the ActionEvent triggered by the user interaction.
   * @feature CoolSupplies Feature Set 5 - Bundle Management
   */
    @FXML
    void doSwitchToGradePage(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ca/mcgill/ecse/coolsupplies/javafx/pages/Grade/GradePage.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) nameLabel.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setMaximized(true);
            stage.setResizable(true);
            stage.setTitle("CoolSupplies");
            stage.setWidth(stage.getMaxWidth());
            stage.setHeight(stage.getMaxHeight());
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}