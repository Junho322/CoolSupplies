package ca.mcgill.ecse.coolsupplies.javafx.controller;

import ca.mcgill.ecse.coolsupplies.controller.*;
    import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
    import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Priority;
import javafx.stage.Modality;
import java.util.List;
import java.util.stream.Collectors;

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

  @FXML
  public void initialize() {
    // Initialize labels with empty values
    nameLabel.setText("Name: ");
    emailLabel.setText("Email: ");
    phoneLabel.setText("Phone: ");

    // Initialize the ComboBox with autocomplete
    setupAutoComplete();
  }


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

  // THIS IS ONLY A HELPER FUNCTION SO I CAN ADD PARENTS TO SYSTEM
  @FXML
  private void handleAddParent(ActionEvent event) {
    String email = "david.zhou3@mail.mcgill.ca";
    String password = "Secure123!";
    String name = "David Zhou";
    int phoneNumber = 1234567;

    String result = CoolSuppliesFeatureSet1Controller.addParent(email, password, name, phoneNumber);

    String email2 = "diddy.wang@mail.mcgill.ca";
    String password2 = "Secure123!";
    String name2 = "Diddy Wang";
    int phoneNumber2 = 1234567;

    String result2 = CoolSuppliesFeatureSet1Controller.addParent(email2, password2, name2, phoneNumber2);

    Alert alert = new Alert(AlertType.INFORMATION);
    alert.setTitle("Add Parent");
    alert.setHeaderText(null);
    alert.setContentText(result);
    alert.showAndWait();
  }

  // THIS IS ONLY A HELPER FUNCTION SO I CAN ADD STUDENTS TO SYSTEM
  @FXML
  private void handleAddStudent(ActionEvent event) {
    CoolSuppliesFeatureSet7Controller.addGrade("1");
    CoolSuppliesFeatureSet7Controller.addGrade("2");
    CoolSuppliesFeatureSet7Controller.addGrade("3");
    CoolSuppliesFeatureSet7Controller.addGrade("4");
    CoolSuppliesFeatureSet7Controller.addGrade("5");
    CoolSuppliesFeatureSet7Controller.addGrade("6");
    CoolSuppliesFeatureSet7Controller.addGrade("7");
    CoolSuppliesFeatureSet7Controller.addGrade("8");

    String name = "Diddy 1";
    String gradeLevel = "1";
    String result = CoolSuppliesFeatureSet2Controller.addStudent(name, gradeLevel);

    String name2 = "Diddy 2";
    String gradeLevel2 = "2";
    String result2 = CoolSuppliesFeatureSet2Controller.addStudent(name2, gradeLevel2);

    String name3 = "Diddy 3";
    String gradeLevel3 = "3";
    String result3 = CoolSuppliesFeatureSet2Controller.addStudent(name3, gradeLevel3);

    String name4 = "Diddy 4";
    String gradeLevel4 = "4";
    String result4 = CoolSuppliesFeatureSet2Controller.addStudent(name4, gradeLevel4);

    String name5 = "Jun Ho";
    String gradeLevel5 = "5";
    String result5 = CoolSuppliesFeatureSet2Controller.addStudent(name5, gradeLevel5);

    String name6 = "Hamza";
    String gradeLevel6 = "6";
    String result6 = CoolSuppliesFeatureSet2Controller.addStudent(name6, gradeLevel6);

    String name7 = "Shayan";
    String gradeLevel7 = "7";
    String result7 = CoolSuppliesFeatureSet2Controller.addStudent(name7, gradeLevel7);

    String name8 = "Jack";
    String gradeLevel8 = "8";
    String result8 = CoolSuppliesFeatureSet2Controller.addStudent(name8, gradeLevel8);

    Alert alert = new Alert(AlertType.INFORMATION);
    alert.setTitle("Add Student");
    alert.setHeaderText(null);
    alert.setContentText(result);
    alert.showAndWait();
  }

  @FXML
  private void handleDeleteParent(ActionEvent event) {
    // Extract the email of the currently displayed parent
    String displayedEmail = emailLabel.getText().replace("Email: ", "").trim();

    // Check if an email is displayed
    if (displayedEmail.isEmpty()) {
      // Show an error if no email is displayed
      Alert alert = new Alert(AlertType.ERROR);
      alert.setTitle("Delete Parent");
      alert.setHeaderText(null);
      alert.setContentText("No parent is selected to delete.");
      alert.showAndWait();
      return;
    }

    // Call the controller method to delete the parent
    String result = CoolSuppliesFeatureSet1Controller.deleteParent(displayedEmail);

    // Show a success or error message based on the result
    Alert alert;
    if (result.equals("Parent deleted successfully")) {
      alert = new Alert(AlertType.INFORMATION);
      alert.setTitle("Delete Parent");
      alert.setHeaderText(null);
      alert.setContentText(result);

      // Clear the UI after deletion
      nameLabel.setText("Name: ");
      emailLabel.setText("Email: ");
      phoneLabel.setText("Phone: ");
    } else {
      alert = new Alert(AlertType.ERROR);
      alert.setTitle("Delete Parent");
      alert.setHeaderText(null);
      alert.setContentText(result);
    }

    alert.showAndWait();
  }

  @FXML
  private void handleChangeParent(ActionEvent event) {
    // Create a custom dialog
    Dialog<String> dialog = new Dialog<>();
    dialog.setTitle("Select Parent");
    dialog.setHeaderText("Choose a parent from the list:");

    // Get list of parents
    List<TOParent> parents = CoolSuppliesFeatureSet1Controller.getParents();

    if (parents.isEmpty()) {
      Alert alert = new Alert(AlertType.INFORMATION);
      alert.setTitle("No Parents");
      alert.setHeaderText(null);
      alert.setContentText("No parents are currently registered in the system.");
      alert.showAndWait();
      return;
    }

    // Create a ListView to display parent emails
    ListView<String> listView = new ListView<>();
    for (TOParent parent : parents) {
      listView.getItems().add(parent.getEmail());
    }

    // Set the preferred size of the ListView
    listView.setPrefWidth(300);
    listView.setPrefHeight(200);

    // Create the dialog's content
    VBox content = new VBox(10);
    content.getChildren().add(listView);
    VBox.setVgrow(listView, Priority.ALWAYS);
    dialog.getDialogPane().setContent(content);

    // Add buttons to the dialog
    ButtonType selectButtonType = new ButtonType("Select", ButtonBar.ButtonData.OK_DONE);
    ButtonType cancelButtonType = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
    dialog.getDialogPane().getButtonTypes().addAll(selectButtonType, cancelButtonType);

    // Enable/Disable Select button depending on whether a parent is selected
    Button selectButton = (Button) dialog.getDialogPane().lookupButton(selectButtonType);
    selectButton.setDisable(true);

    listView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) ->
        selectButton.setDisable(newValue == null)
    );

    // Convert the result to the selected email when the Select button is clicked
    dialog.setResultConverter(dialogButton -> {
      if (dialogButton == selectButtonType) {
        return listView.getSelectionModel().getSelectedItem();
      }
      return null;
    });

    // Show the dialog and handle the result
    dialog.initModality(Modality.APPLICATION_MODAL);
    dialog.showAndWait().ifPresent(selectedEmail -> {
      TOParent selectedParent = CoolSuppliesFeatureSet1Controller.getParent(selectedEmail);
      if (selectedParent != null) {
        // Update the labels with parent information
        nameLabel.setText("Name: " + selectedParent.getName());
        emailLabel.setText("Email: " + selectedParent.getEmail());
        phoneLabel.setText("Phone: " + formatPhoneNumber(selectedParent.getPhoneNumber()));
      }
    });

    String parentEmail = emailLabel.getText().replace("Email: ", "").trim();

    if (parentEmail.isEmpty()) {
      System.out.println("No parent selected.");
      return;
    }
    // Fetch students for the selected parent
    List<TOStudent> students = CoolSuppliesFeatureSet6Controller.getStudentsOfParent(parentEmail);

    // Populate the student cards dynamically
    populateStudentCards(students);
  }

  @FXML
  private void handleSearchStudent(ActionEvent event) {
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
      alert.setContentText("No student is selected.");
      alert.showAndWait();
      return;
    }

    // Call the controller method to add the student to the parent
    String result = CoolSuppliesFeatureSet6Controller.addStudentToParent(studentName, parentEmail);

    // Show a success or error message based on the result
    Alert alert;
    if (result.equals("Student added to parent.")) {
      System.out.println("Success: " + result);

      // Refresh the student cards
      List<TOStudent> students = CoolSuppliesFeatureSet6Controller.getStudentsOfParent(parentEmail);
      populateStudentCards(students);
    } else {
      System.out.println("Error: " + result);
    }
  }

  private String formatPhoneNumber(int phoneNumber) {
    String phoneStr = String.valueOf(phoneNumber);
    // Assuming 7-digit phone number
    return "(" + phoneStr.substring(0, 3) + ") " +
        phoneStr.substring(3, 6) + " " +
        phoneStr.substring(6);
  }
  private static final String[] COLORS = {"#FF4081", "#8E24AA", "#1E88E5", "#03A9F4"};
  private void populateStudentCards(List<TOStudent> students) {
    studentCardContainer.getChildren().clear(); // Assuming `studentCardContainer` is the HBox containing the cards.

    int colorIndex = 0;

    for (TOStudent student : students) {
      VBox card = new VBox(5);
      card.setPrefWidth(270.0);
      card.setMinHeight(300.0);
      card.setStyle("-fx-background-color: " + COLORS[colorIndex] + "; -fx-background-radius: 10; -fx-padding: 8;");
      colorIndex = (colorIndex + 1) % COLORS.length;

      HBox header = new HBox();
      header.setAlignment(Pos.TOP_RIGHT);

      // Add X button
      Button removeButton = new Button("X");
      removeButton.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-font-size: 18px;");
      removeButton.setOnAction(e -> handleRemoveStudent(student.getName()));

      header.getChildren().add(removeButton);

      // Student Details
      Label nameLabel = new Label(student.getName());
      nameLabel.setStyle("-fx-font-size: 32px; -fx-font-weight: bold; -fx-text-fill: white;");
      Separator separator = new Separator();
      separator.setStyle("-fx-background-color: white;");
      Label gradeLabel = new Label("Grade: " + student.getGradeLevel());
      gradeLabel.setStyle("-fx-font-size: 24px; -fx-text-fill: white;");

      // Add elements to card
      card.getChildren().addAll(header, nameLabel, separator, gradeLabel);

      // Add button at the bottom
      Button startOrderButton = new Button("Start Order");
      startOrderButton.setStyle("-fx-background-color: white; -fx-text-fill: black; -fx-font-weight: bold; -fx-background-radius: 20; -fx-font-size: 24px;");
      Button viewOrderButton = new Button("View Order");
      viewOrderButton.setStyle("-fx-background-color: white; -fx-text-fill: black; -fx-font-weight: bold; -fx-background-radius: 20; -fx-font-size: 24px;");
      card.getChildren().add(startOrderButton);
      card.getChildren().add(viewOrderButton);

      HBox.setHgrow(card, Priority.ALWAYS); // Allow dynamic resizing
      studentCardContainer.getChildren().add(card); // Add card to container
    }
  }

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


}