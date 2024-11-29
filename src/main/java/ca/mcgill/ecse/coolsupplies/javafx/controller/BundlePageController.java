package ca.mcgill.ecse.coolsupplies.javafx.controller;

import ca.mcgill.ecse.coolsupplies.application.CoolSuppliesApplication;
import ca.mcgill.ecse.coolsupplies.controller.*;
import ca.mcgill.ecse.coolsupplies.javafx.pages.AdminPageController;
import ca.mcgill.ecse.coolsupplies.model.BundleItem;
import ca.mcgill.ecse.coolsupplies.model.CoolSupplies;
import ca.mcgill.ecse.coolsupplies.model.Grade;
import ca.mcgill.ecse.coolsupplies.model.GradeBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BundlePageController {

    private Stage stage;

    private Scene scene;

    private Parent root;

    @FXML
    private Button settingsButton;

    @FXML
    private Button logoutButton;

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
        populateListView();

        // Set all dropdown menu values
        ArrayList<TOGrade> grades = new ArrayList<>();
        ArrayList<String> gradeNames = new ArrayList<>();
        grades.addAll(CoolSuppliesFeatureSet7Controller.getGrades());
        for (TOGrade grade : grades) {
            gradeNames.add(grade.getLevel());
        }
        gradeChoice.getItems().addAll(gradeNames);
        editGrade.getItems().addAll(gradeNames);

        // Add a listener for bundle selection
        listView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                handleBundleSelection(newVal);
            }
        });

        initializeButtonGraphics();
    }

    @FXML
    private void handleAddButton() {
        String name = nameField.getText();
        String discount = discountField.getText();
        String grade = gradeChoice.getValue();

        if (name.isEmpty() || discount.isEmpty() || grade == null) {
            showErrorAlert("Error", "All fields must be filled to add a bundle.");
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
    private void handleEditItems(ActionEvent event) throws IOException {
        // Correctly initialize the FXMLLoader with the resource location
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ca/mcgill/ecse/coolsupplies/javafx/pages/BundleItemPage.fxml"));
        Parent root = fxmlLoader.load();

        // Get the controller and pass data to it
        BundleItemPageController passer = fxmlLoader.getController();
        passer.getBundleName(selectedBundleName);

        // Set up the scene and stage
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("../resources/styles.css").toExternalForm());
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
    }


    @FXML
    private void handleSaveButton() {
        TOGradeBundle oldBundle = CoolSuppliesFeatureSet4Controller.getBundle(selectedBundleName);
        String oldName = oldBundle.getName();
        String oldDiscount = String.valueOf(oldBundle.getDiscount());
        String oldGrade = oldBundle.getGradeLevel();
        String newName = editBundle.getText();
        String newDiscount = editDiscount.getText();
        String newGrade = editGrade.getValue();

        if (newName.isEmpty() || newDiscount.isEmpty() || newGrade == null) {
            showErrorAlert("Error", "All fields must be filled to edit a bundle.");
            return;
        }

        try {
            int discountValue = Integer.parseInt(newDiscount);
            String result = CoolSuppliesFeatureSet4Controller.updateBundle(
                    selectedBundleName,
                    newName,
                    Integer.parseInt(newDiscount),
                    newGrade
            );
            if (!result.equals("Successfully updated Bundle: " + selectedBundleName)) {
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
            editBundle.clear();
            editDiscount.clear();
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
                populateListView();
                showSuccessAlert("Success", "Bundle deleted successfully.");
            } else {
                // User canceled the action
                System.out.println("Deletion canceled by the user.");
            }
        });
    }


    // Handle bundle selection from the ListView
    private void handleBundleSelection(String selected) {
        selectedBundleName = selected;
        System.out.println("Selected bundle: " + selected);
        TOGradeBundle selectedBundle = CoolSuppliesFeatureSet4Controller.getBundle(selected);
        editBundle.setText(selectedBundle.getName());
        editDiscount.setText(String.valueOf(selectedBundle.getDiscount()));
        editGrade.setValue(selectedBundle.getGradeLevel());
    }

    private void populateListView() {
        listView.getItems().clear();
        List<TOGradeBundle> allBundles = CoolSuppliesFeatureSet4Controller.getBundles();
        System.out.println(allBundles);
        for (TOGradeBundle bundle : allBundles) {
            bundles.add(bundle.getName());
        }
        listView.setItems(bundles);
    }

    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showSuccessAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    void doSwitchToAdminPage(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ca/mcgill/ecse/coolsupplies/javafx/pages/AdminPage.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) nameField.getScene().getWindow();
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

    @FXML
    void doSwitchToOrderPage(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ca/mcgill/ecse/coolsupplies/javafx/pages/Order/OrderPage.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) nameField.getScene().getWindow();
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
    void doSwitchToInventoryPage(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ca/mcgill/ecse/coolsupplies/javafx/pages/InventoryPage.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) nameField.getScene().getWindow();
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
    void doSwitchToParentPage(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ca/mcgill/ecse/coolsupplies/javafx/pages/ParentPage.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) nameField.getScene().getWindow();
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
    void doSwitchToStudentsPage(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ca/mcgill/ecse/coolsupplies/javafx/pages/StudentPage.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) nameField.getScene().getWindow();
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
    void doLogout(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ca/mcgill/ecse/coolsupplies/javafx/LoginPage.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) nameField.getScene().getWindow();
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
    void doSwitchToGradePage(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ca/mcgill/ecse/coolsupplies/javafx/pages/Grade/GradePage.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) nameField.getScene().getWindow();
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
    void doSwitchToShowStudentsPage(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ca/mcgill/ecse/coolsupplies/javafx/pages/ShowStudentsPage.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) nameField.getScene().getWindow();
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

    public void initializeButtonGraphics() {
        ImageView settingsImage = new ImageView("ca/mcgill/ecse/coolsupplies/javafx/resources/settings.png");
        settingsImage.setFitHeight(30);
        settingsImage.setFitWidth(30);
        settingsImage.setEffect(new javafx.scene.effect.ColorAdjust(0, 0, 0.34, 0));

        ImageView logoutImage = new ImageView("ca/mcgill/ecse/coolsupplies/javafx/resources/logout.png");
        logoutImage.setFitHeight(30);
        logoutImage.setFitWidth(30);
        logoutImage.setEffect(new javafx.scene.effect.ColorAdjust(0, 0, 0.34, 0));

        settingsButton.setGraphic(settingsImage);
        settingsButton.setText("");
        settingsButton.setStyle("-fx-background-color: transparent;");
        settingsButton.setPadding(new Insets(0, 8, 0, 0));
        settingsButton.setPrefSize(30, 30);

        logoutButton.setGraphic(logoutImage);
        logoutButton.setText("");
        logoutButton.setStyle("-fx-background-color: transparent;");
        logoutButton.setPadding(new Insets(0, 8, 0, 0));
        logoutButton.setPrefSize(30, 30);
    }
}
